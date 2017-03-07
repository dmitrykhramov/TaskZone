package com.khramovdmitry;

import com.khramovdmitry.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class TaskzoneApplication {

	@Bean
	public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
		return new SecurityConfig();
	}

	public static void main(String[] args) {
		SpringApplication.run(TaskzoneApplication.class, args);
	}
}
