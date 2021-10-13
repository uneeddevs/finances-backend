package com.uneeddevs.finances.security;

import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

class JwtAuthorizationFilterTest {

	private static final String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBtYWlsLmNvbSIsInJvbGVzIjoiW1JPTEVfQURNSU5dIiwiaXNzIjoiY29tLnVuZWVkZGV2cyIsImlkIjoiZW1haWxAbWFpbC5jb20iLCJleHAiOjI2MzQxNjUzMjd9.V0AvuyJ1MAmYwfghlboXmhvX5Ahkz899tO3kzFOzTYjatc6U-juNYaEv5zS4uKZVAOt46WtsHXtIdyVqz8LjRA";
	private AuthenticationManager authenticationManager;
	private JwtUtil jwtUtil;
	private UserService userService;
	private JwtAuthorizationFilter jwtAuthenticationFilter;

	@BeforeEach
	void setup() {
		authenticationManager = mock(AuthenticationManager.class);
		jwtUtil = new JwtUtil(1000000000L, "MOCKED_SECRET");
		userService = mock(UserService.class);
		jwtAuthenticationFilter = new JwtAuthorizationFilter(authenticationManager,
				 jwtUtil, userService);
	}

	@Test
	void testDoInternalFilterExpectedSuccess() throws Exception {
		try(MockedStatic<SecurityContextHolder> mockedContext = Mockito.mockStatic(SecurityContextHolder.class)) {
			final HttpServletRequest request = mock(HttpServletRequest.class);
			final HttpServletResponse response = mock(HttpServletResponse.class);
			final FilterChain chain = mock(FilterChain.class);
			final SecurityContext securityContext = mock(SecurityContext.class);

			mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
			doNothing().when(securityContext).setAuthentication(any());
			when(userService.loadUserByUsername(anyString())).thenReturn(UserMock.mock(true));

			final String authorizationHeader = "Authorization";
			when(request.getHeader(authorizationHeader)).thenReturn("Bearer " + JWT_TOKEN);
			System.out.println(jwtUtil.isValidToken(JWT_TOKEN));
			jwtAuthenticationFilter.doFilterInternal(request, response, chain);
		}
	}
}
