package com.bankingtransactions.bankingendpoints;

import com.bankingtransactions.bankingendpoints.service.MonthlyStatementService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class BankingEndPointsApplication {

	public static void main(String[] args) {

        SpringApplication.run(BankingEndPointsApplication.class, args);
	}

}
