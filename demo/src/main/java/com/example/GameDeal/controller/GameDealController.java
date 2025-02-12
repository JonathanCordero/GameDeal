package com.example.GameDeal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deals")
public class GameDealController {
    
    private final String API_URL = "https://www.cheapshark.com/api/1.0/deals?storeID=1&upperPrice=15";

    @GetMapping
    public List<Map<String, Object>> getDeals() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, List.class);
    }
    
    @GetMapping("/games")
    public String getGames(@RequestParam(required = false, defaultValue = "0") String page, Model model) {
        // Fetch data from CheapShark API
        // Add the data to the model
        model.addAttribute("games", fetchGamesFromCheapShark(page));
        return "gameList";  // Thymeleaf template name
    }

    private List<Game> fetchGamesFromCheapShark(String page) {
        // Your logic to call the API
        return new ArrayList<>();
    }
}
