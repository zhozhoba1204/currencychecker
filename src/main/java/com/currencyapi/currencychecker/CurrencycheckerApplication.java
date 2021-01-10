package com.currencyapi.currencychecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class CurrencycheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencycheckerApplication.class, args);
	}

}
