package com.dev.olivebakery.security;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationTokenFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;

    public AuthenticationTokenFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("=============토큰 검사 필터 실행=============");
        try {
            String token = jwtProvider.resolveToken(getAsHttpRequest(servletRequest));
            if (token != null && jwtProvider.validateToken(token)) {
                SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthenticationByToken(token));
            }
        } catch (JwtException | IllegalArgumentException e){
            log.error("Expired or invalid JWT token");
             HttpServletResponse response = (HttpServletResponse) servletResponse;
             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
             response.getWriter().write("Expired or invalid JWT token");
        } catch (UsernameNotFoundException e){
            log.error("User not found");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private HttpServletRequest getAsHttpRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting an HTTP request");
        }
        return (HttpServletRequest) request;
    }
}
