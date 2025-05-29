package com.example.GameDeal.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.*;
import com.example.GameDeal.repository.UserRepository;
import com.example.GameDeal.model.User;

public class UserDetailsServiceImpl implements UserDetailsService{

	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException{
		User user = userRepository.findByUsername(usernameOrEmail).or
				(()-> userRepository.findByEmail(usernameOrEmail)).orElseThrow
				(()-> new UsernameNotFoundException("User not found: "+ usernameOrEmail));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),Collections.emptyList());
	}
}
