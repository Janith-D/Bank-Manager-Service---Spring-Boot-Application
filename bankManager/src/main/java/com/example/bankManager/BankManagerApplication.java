package com.example.bankManager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank Management App",
				description = "backend rest APIs ",
				version = "v1",
				contact = @Contact(
						name = "Janith Dharmasiri",
						email = "janetdgo2001@gmail.com"
				)
		)
)
public class BankManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankManagerApplication.class, args);
	}

}
