package com.leherryacademy.LeHerryAcademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.leherryacademy.LeHerryAcademy.reponsitory")
@EntityScan("com.leherryacademy.LeHerryAcademy.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class LeHerryAcademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeHerryAcademyApplication.class, args);
	}

}
