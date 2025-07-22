package com.example.GameDeal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.GameDeal.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		return http.authorizeHttpRequests(auth -> auth.requestMatchers("/deals/verify","/deals/newUser","/deals/newUser/**","/deals/verify/**",
				"/deals/emailSent","/deals/gamelist","/deals/game/**","/css/**","/js/**").permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/deals/login").defaultSuccessUrl("/deals/gamelist",true).permitAll())
				.logout(logout -> logout.permitAll()).csrf(csrf -> csrf.disable()).build();
	}
	
	@Bean 
	public AuthenticationManager authentificationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
