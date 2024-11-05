package com.ictk.issuance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing  // Enable JPA Auditing
public class IssuanceMachineServerApplication {

	public static void main(String[] args) {
		log.info("IssuanceMachineServerApplication start.!");
		SpringApplication.run(IssuanceMachineServerApplication.class, args);
	}

}
