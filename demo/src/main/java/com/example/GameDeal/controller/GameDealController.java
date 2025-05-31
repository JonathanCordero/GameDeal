package com.example.GameDeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.GameDeal.model.GameDeals;
import com.example.GameDeal.model.User;
import com.example.GameDeal.repository.GameDealsRepository;
import com.example.GameDeal.service.CheapSharkService;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/deals")  
public class GameDealController {
    
	@Autowired
	private CheapSharkService cheapSharkService;
	
	@Autowired
	private GameDealsRepository gameDealsRepository;
	
    public GameDealController(CheapSharkService cheapSharkService) {
        this.cheapSharkService = cheapSharkService;
    }
    
    @ModelAttribute("loggedInUser")
    public User getLoggedInUser(Authentication authentication) {
    	if (authentication!=null&&authentication.isAuthenticated()) {
    		Object Principal = authentication.getPrincipal();
    		if (Principal instanceof User) {
    			return (User) Principal;
    		}
    	}
    	return null;
    }
    
    @GetMapping("/gamelist")
    public String getGameList(Model model) {
        List<GameDeals> games = cheapSharkService.fetchAndSaveGameDeals(); //This should fetch and then save the games into a repository now.
        Map<String, GameDeals> cheapestDeals = new HashMap<>();

        for (GameDeals deal : games) {
            String normalizedTitle = normalizeTitle(deal.getTitle());

            if (!cheapestDeals.containsKey(normalizedTitle) || deal.getSalePrice() < cheapestDeals.get(normalizedTitle).getSalePrice()) {
                cheapestDeals.put(normalizedTitle, deal);}
        }
        
        model.addAttribute("games", new ArrayList<>(cheapestDeals.values()));
        model.addAttribute("contentTemplate", "gamelist");
        return "layout";
    }
   
    private String normalizeTitle(String title) {
        title = title.toLowerCase().replace(":", "").trim(); 
        String[] words = title.split("\\s+");

        for (int i = 1; i < words.length; i++) {
            if (words[i].equals("edition")) {
                return String.join(" ", Arrays.copyOf(words, i)); 
            }
        }

        return title;
    }
    
    @GetMapping("/game/{id}")
    public String getGameDetails(@PathVariable Long id, Model model) {
        Optional<GameDeals> game = gameDealsRepository.findById(id);
        if (game.isPresent()) {
            model.addAttribute("game", game.get());
            model.addAttribute("contentTemplate", "gamedetails");
            return "layout"; 
        } else {
            return "redirect:/deals/gamelist";
        }
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
    
    /*@PostConstruct // Test to check if gameIDS are being added properly on launch.
    public void checkGameIds() {
        List<GameDeals> games = gameDealsRepository.findAll();
        
        if (games.isEmpty()) {
            System.out.println("No games found in the database!");
        } else {
            for (GameDeals game : games) {
                System.out.println("Game: " + game.getTitle() + " | ID: " + game.getId());
            }
        }
    }*/
    
}
