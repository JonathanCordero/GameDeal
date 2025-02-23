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
import java.util.List;
import java.util.Map;

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
        System.out.println("Fetched Game Deals: " + games); // Log the fetched games
        model.addAttribute("games", games);
        return "gamelist"; // This should match your gamelist.html file name
    }
    
}
