package com.songzi.service;

import com.songzi.config.Constants;
import com.songzi.domain.Authority;
import com.songzi.domain.Department;
import com.songzi.domain.User;
import com.songzi.domain.enumeration.ReviewAction;
import com.songzi.domain.enumeration.ReviewStatus;
import com.songzi.repository.AuthorityRepository;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.UserDTO;
import com.songzi.service.util.RandomUtil;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.errors.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentSerivce;

    @Autowired
    private ReportService reportService;

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
        newUser.setCertDn(userDTO.getCertDn());
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
        if (!StringUtils.isEmpty(userDTO.getCertDn())){
            // 检查证书号是否重复
            Optional<User> optionalUser= userRepository.findByCertDnIs(userDTO.getCertDn());
            if (optionalUser.isPresent()){
                throw new BadRequestAlertException("证书号以及存在，请换一个证书号", this.getClass().getName(), "不允许重复添加证书号");
            }
            user.setCertDn(userDTO.getCertDn());
        }
        if (userDTO.getAuthorities() != null){
            Set<Authority> collect = userDTO.getAuthorities().stream().map(Authority::new).collect(Collectors.toSet());
            user.setAuthorities(collect);
        }
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
        //String encryptedPassword = passwordEncoder.encode();
        //String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        String encryptedPassword = passwordEncoder.encode(userDTO.getLogin());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(false); // 待审核
        user.setReviewStatus(ReviewStatus.CREATE_REVIEW.name());
        // 添加部门
        if (userDTO.getDepartment().getId() != null) {
            user.setDepartment(departmentRepository.findOne(userDTO.getDepartment().getId()));
        }
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
     * @param lastName  last name of user
     * @param email     email id of user
     * @param langKey   language key
     * @param imageUrl  image URL of user
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
     * 审核用户
     * @param id
     * @param reviewStatus
     * @param isActivated
     */
    public void updateUser(Long id, ReviewStatus reviewStatus, boolean isActivated){
        Optional.of(userRepository.findOne(id)).ifPresent(user -> {
            user.setReviewStatus(reviewStatus.name());
            user.setActivated(isActivated);
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
                user.setCertDn(userDTO.getCertDn());
                user.setReviewStatus(userDTO.getReviewStatus());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                // 添加部门
                if (userDTO.getDepartment() != null) {
                    user.setDepartment(departmentRepository.findOne(userDTO.getDepartment().getId()));
                }
                cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
                cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    /**
     * 根据用户登录账号删除用户
     *
     * @param login
     */
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            // admin 和 system 不允许被删。
            if ("admin".equals(user.getLogin()) || "system".equals(user.getLogin())){
                throw new BadRequestAlertException(user.getLogin()+" 用户不能被删除",this.getClass().getName(),user.getLogin()+" 用户不能被删除");
            }
            userRepository.delete(user);
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Deleted User: {}", user);
        });
    }

    /**
     * 删除审核
     * @param login
     */
    public void deleteUserForReview(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            // admin 和 system 不允许被删。
            if ("admin".equals(user.getLogin()) || "system".equals(user.getLogin())){
                throw new BadRequestAlertException(user.getLogin()+" 用户不能被删除",this.getClass().getName(),user.getLogin()+" 用户不能被删除");
            }
            user.setReviewStatus(ReviewStatus.DELETE_REVIEW.name());
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Deleted User: {}", user);
        });
    }

    /**
     * 根据用户ID删除用户
     *
     * @param id
     */
    public void deleteUser(Long id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            // admin 和 system 不允许被删。
            if ("admin".equals(user.getLogin())
                || "system".equals(user.getLogin())
                || "security".equals(user.getLogin()) ){
                throw new BadRequestAlertException(user.getLogin()+" 用户不能被删除",this.getClass().getName(),user.getLogin()+" 用户不能被删除");
            }
            this.deleteUser(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Deleted User: {}", user);
        }
        // 清除用户的报告
        reportService.deleteReportByUser(id);
        log.debug("Deleted User {}'s Report.", user);
    }

    /**
     * 删除审核
     * @param id
     */
    public void deleteUserForReview(Long id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            // admin 和 system 不允许被删。
            if ("admin".equals(user.getLogin()) || "system".equals(user.getLogin())){
                throw new BadRequestAlertException(user.getLogin()+" 用户不能被删除",this.getClass().getName(),user.getLogin()+" 用户不能被删除");
            }
            user.setReviewStatus(ReviewStatus.DELETE_REVIEW.name());
            cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).evict(user.getLogin());
            cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).evict(user.getEmail());
            log.debug("Deleted User: {}", user);
        }
    }

    public void changePassword(String oldpassword, String password) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                // 检查旧密码
                boolean pass = passwordEncoder.matches(oldpassword, user.getPassword());
                if (!pass)
                    throw new InvalidPasswordException();
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
    public Page<UserDTO> getAllManagedAndReviewedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNotAndReviewStatusIs(pageable, Constants.ANONYMOUS_USER,
            ReviewStatus.NORMAL.name()).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedReviewUsersForReview(Pageable pageable) {
        return userRepository.findAllByLoginNotAndReviewStatusEndingWith(pageable, Constants.ANONYMOUS_USER,
            "REVIEW").map(UserDTO::new);
    }
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedRejectUsers(Pageable pageable) {
        return userRepository.findAllByLoginNotAndReviewStatusIs(pageable, Constants.ANONYMOUS_USER,
            ReviewStatus.REJECT.name()).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsersRecords(Pageable pageable) {
        return userRepository.findAllByLoginNotAndReviewStatusIsIn(pageable, Constants.ANONYMOUS_USER,
            ReviewStatus.REJECT.name(),ReviewStatus.NORMAL.name()).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedReviewUsersForCreateReview(Pageable pageable) {
        return userRepository.findAllByLoginNotAndReviewStatusIs(pageable, Constants.ANONYMOUS_USER,
            ReviewStatus.CREATE_REVIEW.name()).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedReviewUsersForDeleteReview(Pageable pageable) {
        return userRepository.findAllByLoginNotAndReviewStatusIs(pageable, Constants.ANONYMOUS_USER,
            ReviewStatus.DELETE_REVIEW.name()).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesWithDepartmentByLogin(String login) {
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
        if (userByEmail.isPresent()) {
            throw new BadRequestAlertException("邮箱已经存在", this.getClass().getName(), "邮箱已经存在");
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
        } else {
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

    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    public User findOne(String login) {
        return userRepository.findOneByLogin(login).get();
    }

    public User findOneByCert(String certDn) {
        Optional<User> optionalUser = userRepository.findOneByCertDn(certDn);
        if (optionalUser.isPresent())
        {
            return optionalUser.get();
        }
        return null;
    }

    /**
     * 根据创建者查出用户信息
     *
     * @param pageable
     * @param login
     * @return
     */
    public Page<UserDTO> findAllByCreatedBy(Pageable pageable, String login) {
        return userRepository.findAllByCreatedByIs(pageable, login).map(UserDTO::new);
    }

    /**
     * 获取子部门用户信息
     *
     * @return 用户列表
     */
    public List<User> getChildDepartmentUserInfo4Statistic(Department department) {
        List<User> userList;
        // 检查用户的权限
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.CHU_ADMIN)) {
            // 如果是处级用户需要获取下级普通用户的信息
            userList = departmentSerivce.getChildDepartmentUserByDepartment(department);
        } else if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.JU_ADMIN)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TING_ADMIN)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.BU_ADMIN)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            // 如果是处级以上的用户需要获取下级管理员统计
            userList = departmentSerivce.getChildDepartmentUserByDepartmentCode(department.getCode() + "____");
            List<User> subAdminUserList = departmentSerivce.getChildDepartmentUserByDepartmentCode(department.getCode() + "__");
            userList.addAll(subAdminUserList);
            // 去掉普通用户的角色
            userList.removeIf(user -> user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.USER)));
        } else {
            throw new BadRequestAlertException("用户没有权限", this.getClass().getName(), "用户没有权限");
        }
        return userList;
    }

    /**
     * 审核用户
     * @param login 登陆用户
     * @param action 动作
     */
    public void reviewUser(String login, ReviewAction action) {
        User user = userRepository.findOneByLogin(login).get();
        switch (action) {
            case REJECT :
                this.updateUser(user.getId(),ReviewStatus.REJECT,user.getActivated());
            break;
            case PASS:
                // 创建审核
                if (ReviewStatus.CREATE_REVIEW.name().equals(user.getReviewStatus())){
                    this.updateUser(user.getId(),ReviewStatus.NORMAL,true);
                }
                // 删除审核
                if (ReviewStatus.DELETE_REVIEW.name().equals(user.getReviewStatus())){
                    this.updateUser(user.getId(),ReviewStatus.NORMAL,false);
                }
                break;
            default:
        }
    }
}
