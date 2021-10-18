package com.uneeddevs.finances.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.dto.CredentialsDTO;
import com.uneeddevs.finances.dto.TokenDTO;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import com.uneeddevs.finances.security.exception.handler.JwtAuthenticationFailureHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   ObjectMapper objectMapper,
                                   JwtUtil jwtUtil) {
        setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            CredentialsDTO credentials = new ObjectMapper().readValue(request.getInputStream(), CredentialsDTO.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), new ArrayList<>());
            return authenticationManager.authenticate(authToken);
        } catch (IOException e){
            throw new AuthenticationFailException("Authentication exception");
        }
    }

    @Override
    public void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                         Authentication auth) throws IOException, ServletException {

        User user = (User) auth.getPrincipal();
        String token = jwtUtil.generateToken(user);
        res.addHeader("Authorization", "Bearer " + token);
        res.addHeader("access-control-expose-headers", "Authorization");
        final TokenDTO tokenDTO = TokenDTO.builder()
                .token(token)
                .expiration(jwtUtil.getExpiration(token))
                .build();
        res.getWriter().append(objectMapper.writeValueAsString(tokenDTO));
        res.setContentType("application/json");
    }

}
