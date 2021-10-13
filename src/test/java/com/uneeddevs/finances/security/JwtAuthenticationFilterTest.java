package com.uneeddevs.finances.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

	private AuthenticationManager authenticationManager;
	private ObjectMapper objectMapper;
	private JwtUtil jwtUtil;
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@BeforeEach
	void setup() {
		authenticationManager = mock(AuthenticationManager.class);
		jwtUtil = new JwtUtil(1000000000000L, "MOCKED_SECRET");
		objectMapper = spy(ObjectMapper.class);
		jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,  objectMapper, jwtUtil);
	}

	@Test
	void testGenerateJwtTokenExpectedSuccess() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse res = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
		Authentication auth = mock(Authentication.class);
		PrintWriter printWriter = mock(PrintWriter.class);
		User user = UserMock.mock(true);

		when(auth.getPrincipal()).thenReturn(user);
		when(res.getWriter()).thenReturn(printWriter);
		when(printWriter.append(anyString())).thenReturn(printWriter);

		jwtAuthenticationFilter.successfulAuthentication(req, res, chain, auth);
	}

	@Test
	void testAttemptAuthenticationExpectedSuccess() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse res = mock(HttpServletResponse.class);

		final String authJson = "{\"email\":\"user@mail.com\", \"password\": \"12345\"}";
		final byte[] bytes = authJson.getBytes(StandardCharsets.UTF_8);
		ServletInputStream servletInputStream = new MockedServletInputStream(bytes);

		when(req.getInputStream()).thenReturn(servletInputStream);

		jwtAuthenticationFilter.attemptAuthentication(req, res);
	}

	@Test
	void testAttemptAuthenticationExpectedAuthenticationFailException() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse res = mock(HttpServletResponse.class);

		when(req.getInputStream()).thenThrow(IOException.class);

		assertThrows(AuthenticationFailException.class, () -> jwtAuthenticationFilter.attemptAuthentication(req, res));
	}

	private static class MockedServletInputStream extends ServletInputStream {

		private byte[] bytes;

		private int lastIndexRetrieved = -1;
		private ReadListener readListener = null;

		public MockedServletInputStream(byte[] bytes) {
			this.bytes = bytes;
		}

		@Override
		public boolean isFinished() {
			return (lastIndexRetrieved == bytes.length-1);
		}

		@Override
		public boolean isReady() {
			// This implementation will never block
			// We also never need to call the readListener from this method, as this method will never return false
			return isFinished();
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			this.readListener = readListener;
			if (!isFinished()) {
				try {
					readListener.onDataAvailable();
				} catch (IOException e) {
					readListener.onError(e);
				}
			} else {
				try {
					readListener.onAllDataRead();
				} catch (IOException e) {
					readListener.onError(e);
				}
			}
		}

		@Override
		public int read() throws IOException {
			int i;
			if (!isFinished()) {
				i = bytes[lastIndexRetrieved+1];
				lastIndexRetrieved++;
				if (isFinished() && (readListener != null)) {
					try {
						readListener.onAllDataRead();
					} catch (IOException ex) {
						readListener.onError(ex);
						throw ex;
					}
				}
				return i;
			} else {
				return -1;
			}
		}
	}


}
