package com.uneeddevs.finances.security.handler;

import com.uneeddevs.finances.security.exception.handler.JwtAuthenticationFailureHandler;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthenticationFailureHandlerTest {


	@Test
	void testOnAuthenticationFailureExpectedSuccess() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse res = mock(HttpServletResponse.class);
		AuthenticationException exception = mock(AuthenticationException.class);
		PrintWriter printWriter = mock(PrintWriter.class);
		JwtAuthenticationFailureHandler failureHandler = new JwtAuthenticationFailureHandler();

		when(res.getWriter()).thenReturn(printWriter);

		assertDoesNotThrow(() -> failureHandler.onAuthenticationFailure(req, res, exception));

	}

	@Test
	void testOnAuthenticationFailureExpectedIOException() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse res = mock(HttpServletResponse.class);
		AuthenticationException exception = mock(AuthenticationException.class);
		JwtAuthenticationFailureHandler failureHandler = new JwtAuthenticationFailureHandler();

		when(res.getWriter()).thenThrow(IOException.class);

		assertThrows(IOException.class, () -> failureHandler.onAuthenticationFailure(req, res, exception));

	}

	@Test
	void testJsonExpectedNotEmpty() throws Exception {
		JwtAuthenticationFailureHandler failureHandler = new JwtAuthenticationFailureHandler();
		final Class<JwtAuthenticationFailureHandler> clazz = JwtAuthenticationFailureHandler.class;
		final Method json = clazz.getDeclaredMethod("json");
		json.setAccessible(true);
		final String invoke = (String)json.invoke(failureHandler);
		assertNotNull(invoke, "Invoke cannot be null");
	}

}
