package com.example.GameDeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.GameDeal.config.SecurityConfig;
import com.example.GameDeal.model.User;
import com.example.GameDeal.repository.UserRepository;
import com.example.GameDeal.service.EmailService;
import com.example.GameDeal.service.PendingUserService;

@Controller
@RequestMapping("/deals")
public class UserRegistrationController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PendingUserService pendingUserService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/newUser")
	public String showNewUserPage(Model model) {
		model.addAttribute("contentTemplate","newUser");
		return "layout";
	}
	
	 @PostMapping("/newUser")
	 public String handleNewUserRegistration(@RequestParam String username,@RequestParam String email,@RequestParam String password,Model model) {
	        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
	            model.addAttribute("error", "Username or email already exists!");
	            model.addAttribute("contentTemplate", "newUser");
	            return "layout";
	        }
	        String secure = passwordEncoder.encode(password);
	        User newUser = new User(username, email, secure);
	        String token = pendingUserService.savePendingUser(newUser);
	        String verificationLink = "http://localhost:8080/deals/verify?token=" + token;
	        emailService.sendVerificationEmail(email, token, verificationLink);

	        return "redirect:/deals/emailSent";
	    }
	 
	 @GetMapping("/verify")
	 public String verifyUser(@RequestParam("token") String token, Model model) {
		 System.out.println("Token received: " + token);
		 User verifiedUser = pendingUserService.confirmUser(token);
		 System.out.println("Verified user: " + (verifiedUser != null ? verifiedUser.getUsername() : "null"));
		 
		 if (verifiedUser == null) {
			 model.addAttribute("error", "Verification failed or has expired");
			 model.addAttribute("contentTemplate", "verificationFailed");
			 return "layout";
		 }
		 
		 if (userRepository.findByUsername(verifiedUser.getUsername()).isPresent() || userRepository.findByEmail(verifiedUser.getEmail()).isPresent()) {
			 model.addAttribute("error", "Verification failed or has expired");
			 model.addAttribute("contentTemplate", "verificationFailed");
			 return "layout";
		 }
		 System.out.println("Saving to DB: " + verifiedUser.getUsername()); 
		 userRepository.save(verifiedUser);
		 System.out.println("Saving to DB: " + verifiedUser.getUsername());
		 
		 model.addAttribute("username", verifiedUser.getUsername());
		 model.addAttribute("contentTemplate", "verificationSuccess");
		 return "layout";
	 }
	 
	 @GetMapping("/emailSent")
	 public String showEmailSentPage() {
		return "emailSent"; 
	 }
	
}
