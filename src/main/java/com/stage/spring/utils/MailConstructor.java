package com.stage.spring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.stage.spring.entity.User;

@Component
public class MailConstructor {
	@Autowired
	private Environment env;
public SimpleMailMessage constructNewUserEmail(User user, String password){
	String message="\nPlease use the following credentials to log in and edit your personal information including your own password."
			+"\nUsername:"+ user.getUserName()+"\nPassword:"+password;
	SimpleMailMessage email = new SimpleMailMessage();
	email.setTo(user.getEmail());
	email.setSubject("Forgot password");
	email.setText(message);
	email.setFrom(env.getProperty("support.email"));
	return email;
	
}}
