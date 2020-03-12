package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.enumeration.ReviewAction;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.service.UserService;
import com.songzi.service.dto.UserDTO;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewResource {
    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    @Autowired private UserService userService;

    @GetMapping("/review/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.SECURITY})
    @ApiOperation("获取所有需要待审核的用户列表")
    public ResponseEntity<List<UserDTO>> getAllUsersReview(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedReviewUsersForReview(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/review/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/review/reject/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.SECURITY})
    @ApiOperation("获取所有拒绝审核的用户列表")
    public ResponseEntity<List<UserDTO>> getAllUsersReject(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedRejectUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/review/reject/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/review/create/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.SECURITY})
    @ApiOperation("获取待创建的用户列表")
    public ResponseEntity<List<UserDTO>> getCreateUserReview(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedReviewUsersForCreateReview(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/review/create/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/review/delete/users")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.SECURITY})
    @ApiOperation("获取待删除的用户列表")
    public ResponseEntity<List<UserDTO>> getDeleteUserReview(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedReviewUsersForDeleteReview(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/review/delete/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/review/user")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.SECURITY})
    @ApiOperation("审核添加/删除用户的操作")
    public ResponseEntity<Void> reviewUser(@RequestParam("login") String login,
                                           @RequestParam("action") ReviewAction action){
        log.debug("{}, parameters: login={}, action={}","/review/user",login,action.name());
        userService.reviewUser(login,action);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("review.user", action.name())).build();
    }
}
