package com.github.mattiolileo.keycloak;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Configuration
@ImportResource("classpath:application-context.xml")
public class ManipulateUserApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ManipulateUserApplication.class, args);

		KeycloakImpersonationService service = context.getBean(KeycloakImpersonationService.class);
		//List first 100 users
		System.out.println("\nListing first 100 users");
		List<String> userList = service.listUsers();

		//Let's impersonate a user
		System.out.println("\nLet's impersonate bob");
		String token = service.impersonateUser("bob");
	}
}
