package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.songzi.domain.User;
import com.songzi.security.jwt.JWTConfigurer;
import com.songzi.security.jwt.TokenProvider;
import com.songzi.service.UserService;
import com.songzi.web.rest.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private static final Logger LOG = LoggerFactory.getLogger(UserJWTController.class);

    private static final String COOKIE_USER_NAME_PINYIN = "KOAL_CERT_ALIAS";

    @Autowired
    private UserService userService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        Authentication authentication = null;
        try {
            authentication = this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw ex;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/authenticate/cert")
    @Timed
    public ResponseEntity<JWTToken> authorizeByCert(HttpServletRequest request) {
        // 1. 从cookie中拿到certDn信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (COOKIE_USER_NAME_PINYIN.equals(cookieName)) {
                    String certDn = cookie.getValue();
                    LOG.debug("cert user name: {}", certDn);
                    User user = userService.findOneByCert(certDn);
                    if (user != null) {
                        // 加载用户权限
                        User authorizedUser = userService.getUserWithAuthorities(user.getId()).get();
                        // 封装权限集合
                        List<GrantedAuthority> grantedAuthorities = authorizedUser.getAuthorities().stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toList());
                        // 封装用户
                        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(authorizedUser.getLogin(), "", grantedAuthorities);
                        // 授权
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities);
                        // 生产jwt
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String jwt = tokenProvider.createToken(authentication, false);
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
                        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
                    }
                }
            }
        }

        // 认证失败
        return new ResponseEntity<>(null, null, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
