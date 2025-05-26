package com.example.GameDeal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mail;
	
	public void sendVerificationEmail(String email, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("CordMJon25@gmail.com");
		message.setTo(email);
		message.setSubject(subject);
		message.setText(body);
		mail.send(message);
	}
}
