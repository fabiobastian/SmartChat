package com.cloudhumans.smartchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.cloudhumans.smartchat")
@SpringBootApplication
public class SmartChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartChatApplication.class, args);
	}

}
