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
import java.util.stream.Collectors;

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
        
        gameDealsRepository.saveAll(fetchedGames);

        dealChecker(fetchedGames);
        
        return fetchedGames;
    }
    
    public void dealChecker(List<GameDeals>fetchedGames) {
    	List<GameDeals>current = gameDealsRepository.findAll();
    	Set<String> fetchedDealsIds = fetchedGames.stream().map(GameDeals::getDealID).collect(Collectors.toSet());
    	List<GameDeals> deletingDeals = current.stream().filter(deal -> !fetchedDealsIds.contains(deal.getDealID())).collect(Collectors.toList());
    	gameDealsRepository.deleteAll(deletingDeals);
    	/*System.out.println("size of fetched deals is " + fetchedGames.size());
    	System.out.println("size of currentdb " + current.size());
    	System.out.println("Deleting " + deletingDeals.size() + " expired deals.");
    	Just meant to log sizes to make deals were deleted properly
    	*/
    	
    }
    

}
