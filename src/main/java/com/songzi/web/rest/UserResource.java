package com.songzi.web.rest;

import com.songzi.config.Constants;
import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.User;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.DepartmentService;
import com.songzi.service.MailService;
import com.songzi.service.UserService;
import com.songzi.service.dto.DepartmentDTO;
import com.songzi.service.dto.UserDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.errors.CertDNAlreadyUsedException;
import com.songzi.web.rest.errors.EmailAlreadyUsedException;
import com.songzi.web.rest.errors.LoginAlreadyUsedException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    @Autowired
    private DepartmentService departmentSerivce;

    public UserResource(UserRepository userRepository, UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException       if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BU_ADMIN, AuthoritiesConstants.TING_ADMIN, AuthoritiesConstants.CHU_ADMIN, AuthoritiesConstants.JU_ADMIN})
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else if (userRepository.findOneByCertDn(userDTO.getCertDn()).isPresent()) {
            throw new CertDNAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BU_ADMIN, AuthoritiesConstants.TING_ADMIN, AuthoritiesConstants.CHU_ADMIN, AuthoritiesConstants.JU_ADMIN})
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        existingUser = userRepository.findOneByCertDn(userDTO.getCertDn());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))){
            throw new CertDNAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
//        userService.deleteUser(login);
        userService.deleteUserForReview(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }

    @DeleteMapping("/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BU_ADMIN, AuthoritiesConstants.TING_ADMIN, AuthoritiesConstants.CHU_ADMIN, AuthoritiesConstants.JU_ADMIN})
    @ApiOperation("删除用户")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        log.debug("REST request to delete User: {}", id);
//        userService.deleteUser(id);
        userService.deleteUserForReview(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", id + "")).build();
    }

    /**
     * 查询用户的能访问的机构
     *
     * @return
     */
    @GetMapping("/users/departments")
    @Timed
    @ApiOperation(value = "查询用户的机构")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsByUser() {
        List<DepartmentDTO> page = departmentSerivce.finAllByUser();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * 根据用户的登陆名获取用户的创建的部门
     *
     * @return
     */
    @GetMapping("/users/created/departments")
    @Timed
    @ApiOperation("查询用户创建的机构")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsByCreatedUser(@RequestParam("login") String login) {
        List<DepartmentDTO> departmentDTOList = null;
        if (StringUtils.isNotEmpty(login)) {
            User user = userService.findOne(login);
            if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
                departmentDTOList = departmentSerivce.findDepartmentForAdmin();
            }
            else if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.BU_ADMIN)) {
                // 查询出省的机构
                departmentDTOList= departmentSerivce.findAllByProviceDept();

            } else {
                departmentDTOList = new ArrayList<>();
            }
            List<DepartmentDTO> departmentDTOList2 = departmentSerivce.findAllByCreatedUser(user);
            if (departmentDTOList2!=null) {
                departmentDTOList.addAll(departmentDTOList2);
            }
        }
        return new ResponseEntity<>(departmentDTOList, HttpStatus.OK);
    }

    /**
     * 根据创建者查询用户信息
     *
     * @param pageable
     * @param login
     * @return
     */
    @GetMapping("/users/createdBy")
    @Timed
    @ApiOperation("查询某个创建者的用户")
    public ResponseEntity<List<UserDTO>> getAllUserByCreated(Pageable pageable, @RequestParam String login) {
        final Page<UserDTO> page = userService.findAllByCreatedBy(pageable, login);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users/departments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
