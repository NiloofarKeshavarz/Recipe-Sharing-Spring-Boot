package com.example.eatwhat.config;


import com.example.eatwhat.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/user/").hasRole("USER")
//                .antMatchers("/manager/").hasRole("ADMIN")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
////                .loginPage("/login")
//                .usernameParameter("username")
//                .successHandler(successHandler())
//                .failureUrl("/login-error")
//                .permitAll()
//                .and()
//                .logout().logoutSuccessUrl("/").permitAll();


        final UserLoginFilter filter = new UserLoginFilter(
                authenticationManagerBean()
        );
        http
                .csrf().disable()
                .formLogin(login->{
                    login.loginPage("/login")
                    ;
                })
                .logout(logout->{
                    logout.logoutSuccessUrl("/")
                    ;
                })
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception->{
                    exception.accessDeniedPage("/login/access-denied");
                })
                .authorizeRequests(config->{
                    config
                            .antMatchers("/").permitAll()
                            .antMatchers("/home/*").permitAll()
                            .antMatchers("/login").permitAll()
                            .antMatchers("/error").permitAll()
                            .antMatchers("/signup/*").permitAll()
                            .antMatchers("/user/signup/*").permitAll()
                            .antMatchers("/recipe/**").permitAll()
                            .antMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                            .antMatchers("/manager/**").hasAuthority("ROLE_ADMIN")
                    ;
                })
        ;


    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


//    @Bean
//    public AuthenticationSuccessHandler successHandler() {
//        return new LoginSuccessHandler("/defaultUrl");
//    }
}
