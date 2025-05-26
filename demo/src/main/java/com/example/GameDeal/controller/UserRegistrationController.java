package com.example.GameDeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.GameDeal.model.User;
import com.example.GameDeal.repository.UserRepository;
import com.example.GameDeal.service.PendingUserService;

@Controller
@RequestMapping("/deals")
public class UserRegistrationController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PendingUserService pendingUserService;
	
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
	        User newUser = new User(username, email, password); // future iterations should hash the password for security protection.
	        userRepository.save(newUser);

	        return "redirect:/deals/verify";
	    }
	
}
