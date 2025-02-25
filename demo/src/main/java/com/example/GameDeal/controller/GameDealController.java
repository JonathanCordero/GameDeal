package com.example.GameDeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.GameDeal.model.GameDeals;
import com.example.GameDeal.service.CheapSharkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/deals")  
public class GameDealController {
    
	@Autowired
	private CheapSharkService cheapSharkService;
	
	
    public GameDealController(CheapSharkService cheapSharkService) {
        this.cheapSharkService = cheapSharkService;
    }
    
    @GetMapping("/gamelist")
    public String getGameList(Model model) {
        List<GameDeals> games = cheapSharkService.getGameDeals();
        //System.out.println("Fetched Game Deals: " + games); // Log the fetched games
        Map<String, GameDeals> cheapestDeals = new HashMap<>();

        for (GameDeals deal : games) {
            String normalizedTitle = normalizeTitle(deal.getTitle());

            if (!cheapestDeals.containsKey(normalizedTitle) || deal.getSalePrice() < cheapestDeals.get(normalizedTitle).getSalePrice()) {
                cheapestDeals.put(normalizedTitle, deal);}
        }
        
        model.addAttribute("games", new ArrayList<>(cheapestDeals.values()));
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
    
}
