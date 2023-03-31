package com.stage.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.stage.spring.entity.Mail;
@Service
public class ServiceMail implements IServiceMail{
	private JavaMailSender javaMailSender;
	@Autowired
	public ServiceMail(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendCodeByMail(Mail mail) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("naddoun51@gmail.com");
		simpleMailMessage.setTo(mail.getTo());
		simpleMailMessage.setSubject("Code active");
		simpleMailMessage.setText(mail.getCode());
		javaMailSender.send(simpleMailMessage);
		
	}

}
