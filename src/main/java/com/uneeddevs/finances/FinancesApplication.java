package com.uneeddevs.finances;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "Finances API",
		description = "Finance Service",
		contact = @Contact(
				name = "U Need Devs",
				email = "uneeddevs@gmail.com"
		),
		version = "0.1"
))
@SpringBootApplication
public class FinancesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancesApplication.class, args);
	}

}
