package com.example.GameDeal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.GameDeal.model.PendingEmailChange;
import com.example.GameDeal.model.User;
import com.example.GameDeal.repository.UserRepository;
import com.example.GameDeal.service.EmailService;
import com.example.GameDeal.service.PendingUserService;

@Controller
@RequestMapping("/deals")
public class UserActionController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PendingUserService pendingUserService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@ModelAttribute("loggedInUser")
    public User getLoggedInUser(Authentication authentication) {
    	if (authentication!=null&&authentication.isAuthenticated()) {
    		Object Principal = authentication.getPrincipal();
    		if (Principal instanceof UserDetails) {
    			String username = ((UserDetails)Principal).getUsername();
    			return userRepository.findByUsername(username).orElseGet(()->userRepository.findByEmail(username).orElse(null));
    		}
    	}
    	return null;
    }
	
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
	 
	 @GetMapping("/settings")
	    public String showSettings(Model model, Authentication authentication) {
	    	model.addAttribute("user", getLoggedInUser(authentication));
	    	model.addAttribute("contentTemplate","settings");
	    	return "layout";
	    }
	    
	    @PostMapping("/settings")
	    public String updateUserSettings(@RequestParam("currentPassword") String currentPassword, 
	    		@RequestParam(value="profilePic", required=false) MultipartFile profilePic,
	    		@RequestParam(value="newPassword", required = false) String newPassword, 
	    		@RequestParam(value="newEmail", required=false) String newEmail, Authentication authentication, Model model ) {
	    	User user = getLoggedInUser(authentication);
	    	
	    	if(!passwordEncoder.matches(currentPassword,user.getPassword())) {
	    		model.addAttribute("error", "Password does not match!");
	    		model.addAttribute("contentTemplate", "settings");
	    		return "layout";
	    	}
	    	
	    	if(newPassword!=null && !newPassword.isBlank()) {
	    		user.setPassword(passwordEncoder.encode(newPassword));
	    	}
	    	
	    	if(newEmail!=null && !newEmail.isBlank()) {
	    		if(newEmail.equals(user.getEmail())) {
	    			model.addAttribute("error", "You have entered the current email address.");
	    		}
	    		String token = pendingUserService.savePendingEmailChange(user.getId(), newEmail);
	    		String confirmLink = "http://localhost:8080/deals/confirmEmailChange?token=" + token;
	    		emailService.sendVerificationEmail(newEmail, "Email Change Confirmation", confirmLink);
	    		model.addAttribute("success", "A confirmation email has been sent to your new email address.");
	    	}
	    	
	    	if(profilePic!= null && !profilePic.isEmpty()) {
	    		try {
	    			user.setProfilePic(profilePic.getBytes());
	    			user.setProfilePicType(profilePic.getContentType());
	    		}
	    		catch(IOException e) {
	    			e.printStackTrace();
	    			model.addAttribute("error","Failed to upload image");
	    			model.addAttribute("contentTemplate","settings");
	    			return "layout";
	    		}
	    	}
	   		userRepository.save(user);
	   		
	   		model.addAttribute("success", "Profile updated Successfully!");
	    	model.addAttribute("user",user);
	    	model.addAttribute("contentTemplate","settings");
	    	return "layout";
	    }
	    @GetMapping("/profilePic/{userId}")
	    @ResponseBody
	    public ResponseEntity<byte[]> getProfilePic(@PathVariable("userId") Long userId) throws IOException{
	    	Optional<User> user = userRepository.findById(userId);
	    	if(user.isPresent()) {
	    		byte[] image = user.get().getProfilePic();
	    		String contentType = user.get().getProfilePicType();
		    	if (image !=null && image.length>0) {
		    		HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.parseMediaType(contentType != null ? contentType : "image/png"));
		            return new ResponseEntity<>(image, headers, HttpStatus.OK);
		    	}
	    	}
	    	
	    	try (InputStream in = getClass().getResourceAsStream("/static/img/defaultProfilePic.png")) {
	            byte[] defaultImg = in.readAllBytes();

	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.IMAGE_PNG);
	            return new ResponseEntity<>(defaultImg, headers, HttpStatus.OK);
	        } 
	    	catch (IOException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @GetMapping("/confirmEmailChange")
	    public String confirmEmailChange(@RequestParam("token") String token, Model model) {
	    	PendingEmailChange change = pendingUserService.confirmEmailChange(token);
	    	
	    	if (change == null) {
	    		model.addAttribute("error", "Email confirmation failed or has expired!");
	    		model.addAttribute("contentTemplate","settings");
	    		return "layout";
	    	}
	    	User user = userRepository.findById(change.getUserId()).orElse(null);
	    	if (user ==null) {
	    		model.addAttribute("error", "User not found!");
	    		model.addAttribute("contentTemplate", "settings");
	    		return "layout";
	    	}
	    	user.setEmail(change.getNewEmail());
	    	userRepository.save(user);
	    	
	    	model.addAttribute("success", "Email confirmation verified!");
	    	model.addAttribute("user", user);
	    	model.addAttribute("contentTemplate", "settings");
	    	return "layout";
	    }
	    
	    @GetMapping("/wishlist")
	    public String showWishlist(Model model, Authentication authentication) {
	    	model.addAttribute("user", getLoggedInUser(authentication));
	    	return "wishlist";
	    }
	    
	    @GetMapping("/login")
	    public String showLoginPage(Model model) {
	    	model.addAttribute("contentTemplate", "login");
	        return "layout";
	    }
	    
	    @PostMapping("/login")
	    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
	        model.addAttribute("contentTemplate", "success");
	        return "layout";  
	    }
	    
	    @GetMapping("/logout")
	    public String logout() {
	    	return "layout";
	    }
	
}
