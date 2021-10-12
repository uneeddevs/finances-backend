package com.uneeddevs.finances.security;

import com.uneeddevs.finances.service.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityMock {

    @MockBean
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    protected UserService userService;

    @MockBean
    protected JwtUtil jwtUtil;
}
