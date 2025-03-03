package com.example.GameDeal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.GameDeal.model.GameDeals;
import com.example.GameDeal.repository.GameDealsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class CheapSharkService {
    private final String CHEAPSHARK_API_URL = "https://www.cheapshark.com/api/1.0/deals";
    private final GameDealsRepository gameDealsRepository;
    private final RestTemplate restTemplate;

    public CheapSharkService(GameDealsRepository gameDealsRepository) {
    	this.gameDealsRepository = gameDealsRepository;
    	this.restTemplate = new RestTemplate();
    }
    
    public List<GameDeals> getGameDeals() {
    	try {
    		ResponseEntity<GameDeals[]>response = restTemplate.exchange(CHEAPSHARK_API_URL, HttpMethod.GET,null,
    				new ParameterizedTypeReference<GameDeals[]>() {});
    		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
            }
    	}
    	catch(Exception e) {
    		 System.err.println("Error fetching deals from CheapShark: " + e.getMessage());
    	}
    	return Collections.emptyList();
    }

    @Transactional
    public List<GameDeals> fetchAndSaveGameDeals() {
        List<GameDeals> fetchedGames = getGameDeals(); 
        
        if (fetchedGames.isEmpty()) {
            return Collections.emptyList();
        }
        
        Set<String> existingTitles = new HashSet<>(gameDealsRepository.findAllTitlesAndStores());

        List<GameDeals> newGames = new ArrayList<>();
        for (GameDeals game : fetchedGames) {
            String uniqueKey = game.getTitle() + "|" + game.getStore();
            if (!existingTitles.contains(uniqueKey)) {
                newGames.add(game);
            }
        }

        // It will Save only new games
        if (!newGames.isEmpty()) {
            gameDealsRepository.saveAll(newGames);
        }

        return gameDealsRepository.findAll();
    }

}
