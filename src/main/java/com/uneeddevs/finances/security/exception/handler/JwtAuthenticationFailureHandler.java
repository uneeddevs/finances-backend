package com.uneeddevs.finances.security.exception.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(401);
		response.setContentType("application/json");
		response.getWriter().append(json());
	}

	private String json() {
		LocalDateTime date = LocalDateTime.now();
		return "{\"time\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Unauthorized\", "
				+ "\"message\": \"Invalid username or password\", " + "\"path\": \"/login\"}";
	}
}
