package com.stage.spring.configuration;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtility {
	private static final String SALT = "salt";

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}

	@Bean
	public static String randomPassword() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder salt = new StringBuilder();
		Random rn = new Random();
		while (salt.length() < 18) {
			int index = (int) (rn.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();
	}
}
