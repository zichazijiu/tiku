package com.songzi.service;

import com.songzi.config.CacheConfiguration;
import com.songzi.domain.Authority;
import com.songzi.domain.User;
import com.songzi.repository.AuthorityRepository;
import com.songzi.config.Constants;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.util.RandomUtil;
import com.songzi.service.dto.UserDTO;

import com.songzi.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Get the userId of the current user.
     *
     * @return the userId of the current user
     */
    public Long getCurrentUserId() {
        String login = SecurityUtils.getCurrentUserLogin().get();
        Long userId = userRepository.findOneByLogin(login).get().getId();
        return userId;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(newUser.getLogin());
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(newUser.getEmail());
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findOne)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String password) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
        }
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    public User createUserByExaminer(UserDTO userDTO) {
        Optional<User> userByEmail = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if(userByEmail.isPresent()){
            throw new BadRequestAlertException("邮箱已经存在",this.getClass().getName(),"邮箱已经存在");
        }
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findOne)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }else{
            Set<Authority> authorities = new HashSet<>();
            authorities.add(authorityRepository.findOne("ROLE_EXAMINER"));
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode("666666");
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public User findOne(Long id){
        return userRepository.findOne(id);
    }
}
