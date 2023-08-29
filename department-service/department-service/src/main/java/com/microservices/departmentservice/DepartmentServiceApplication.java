package com.microservices.departmentservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Department Service",
				description = "Rest Api Doc",
				version = "v1.0",
				contact = @Contact(
						name = "Maxic01",
						email = "maxi1998calvo@gmail.com",
						url = "https://github.com/maxic01"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://github.com/maxic01"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Department Service",
				url = "https://github.com/maxic01"
		)
)
public class DepartmentServiceApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(DepartmentServiceApplication.class, args);
	}

}
