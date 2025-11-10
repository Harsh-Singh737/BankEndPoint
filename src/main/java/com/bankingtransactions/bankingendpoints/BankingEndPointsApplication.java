package com.bankingtransactions.bankingendpoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingEndPointsApplication {

	public static void main(String[] args) {

        SpringApplication.run(BankingEndPointsApplication.class, args);
	}

}
