package com.uneeddevs.finances.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.security.JwtAuthenticationFilter;
import com.uneeddevs.finances.security.JwtAuthorizationFilter;
import com.uneeddevs.finances.security.JwtUtil;
import com.uneeddevs.finances.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_MATCHERS_POST = {"/users", "/login"};
    private static final String[] PUBLIC_MATCHERS_GET = {"/swagger-ui/**", "/documentation/**", "/documentation-api"};
    private static final String[] ADMIN_MATCHERS = {"/users/page"};

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable();
        http.authorizeRequests()
                .antMatchers(ADMIN_MATCHERS).hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST)
                .permitAll()
                .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET)
                .permitAll()
                .anyRequest().authenticated();

        http.addFilter(new JwtAuthenticationFilter(authenticationManager(), objectMapper, jwtUtil));
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtUtil, userService));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

}
