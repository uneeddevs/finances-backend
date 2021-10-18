package com.uneeddevs.finances.controller;

import com.uneeddevs.finances.dto.CredentialsDTO;
import com.uneeddevs.finances.dto.TokenDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/login")
@Tag(name = "Login", description = "Login endpoint")
public class LoginController {

	@PostMapping
	public ResponseEntity<TokenDTO> fakeLogin(@RequestBody CredentialsDTO credentialsDTO) {
		throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
	}

}
