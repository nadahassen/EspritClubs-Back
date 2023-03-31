package com.stage.spring.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stage.spring.JwtAndAuthConf.ConfirmationToken;
import com.stage.spring.repository.ConfirmationTokenRepository;



@Service
public class ConfirmationTokenService {
	@Autowired
	  ConfirmationTokenRepository confirmationTokenRepository;

	    public void saveConfirmationToken(ConfirmationToken token) {
	        confirmationTokenRepository.save(token);
	    }

	    public Optional<ConfirmationToken> getToken(String token) {
	        return confirmationTokenRepository.findByToken(token);
	    }

	    public int setConfirmedAt(String token) {
	        return confirmationTokenRepository.updateConfirmedAt(
	                token, LocalDateTime.now());
	    }
}
