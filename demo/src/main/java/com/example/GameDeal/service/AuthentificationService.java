package com.example.GameDeal.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {

	private final AuthenticationManager authManager;
	private final UserDetailsService userDetailService;
	
	public AuthentificationService(AuthenticationManager authManager, UserDetailsService userDetailsService) {
		this.authManager = authManager;
		this.userDetailService = userDetailsService;
	}
}
