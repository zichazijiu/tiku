package com.songzi.security.jwt;

import com.songzi.domain.User;
import com.songzi.service.UserService;
import com.songzi.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JWTFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);

    private TokenProvider tokenProvider;

    private UserService userService;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            if (!resolveCookies(httpServletRequest,authentication)){
                ((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
                ((HttpServletResponse) servletResponse).setHeader("Authorization", "");
                ((HttpServletResponse) servletResponse).setStatus(401);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private Boolean resolveCookies(HttpServletRequest request,Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return false;
        }
        for (Cookie cookie: cookies) {
            if (cookie != null){
                if ("KOAL_CERT_CN".equals(cookie.getName())) {
                    String dn = cookie.getValue();
                    if (userService == null) {
                        userService = SpringContextUtils.getBean(UserService.class);
                    }
                    User user = userService.findOneByCert(dn);
                    if (user != null){
                        LOG.debug("KOAL_CERT_CN is {}, login is {}, principal is {}",dn,user.getLogin(),authentication.getName());
                        if (user.getLogin().equals(authentication.getName())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
