package com.induce.profitability_calculation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ProfitabilityCalculationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfitabilityCalculationServiceApplication.class, args);
	}

}
