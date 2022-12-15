package com.example.eatwhat.config;

import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UserLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public UserLoginFilter(
            AuthenticationManager authenticationManager
    ){
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
        this.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        this.setAuthenticationFailureHandler(new LoginFailureHandler());
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLogin userLogin = UserLogin.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .site(request.getParameter("site"))
                .build();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userLogin.getUsername(), userLogin.getPassword(), null
        );
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException
    {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException
    {
        super.unsuccessfulAuthentication(request, response, failed);
    }

}
