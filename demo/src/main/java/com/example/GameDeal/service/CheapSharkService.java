package com.example.GameDeal.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.GameDeal.model.GameDeals;
import com.example.GameDeal.repository.GameDealsRepository;
import com.example.GameDeal.repository.StoreRepository;
import com.example.GameDeal.model.Store;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class CheapSharkService {
	@Autowired
	private StoreRepository storeRepository;
    private final String CHEAPSHARK_API_URL = "https://www.cheapshark.com/api/1.0/deals";
    private final GameDealsRepository gameDealsRepository;
    private final RestTemplate restTemplate;
    private final RestTemplate storeTemplate;

    public CheapSharkService(GameDealsRepository gameDealsRepository, 
    		StoreRepository storeRepository) {
    	this.gameDealsRepository = gameDealsRepository;
    	this.storeRepository=storeRepository;
    	this.restTemplate = new RestTemplate();
    	this.storeTemplate = new RestTemplate();
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
        for (GameDeals deal : fetchedGames) {
        	Store store = storeRepository.findById(deal.getStoreID()).orElse(null);
        	deal.setStore(store);
        }
        List<GameDeals> dupeChecked = new ArrayList<>();
        for(GameDeals deal : fetchedGames) {
        	if(!gameDealsRepository.existsByDealID(deal.getDealID())) {
        		dupeChecked.add(deal);
        	}
        }
        gameDealsRepository.saveAll(dupeChecked);
        List<GameDeals>current = gameDealsRepository.findAll();
        dealChecker(current,fetchedGames);
        
        return current;
    }
    
    public void dealChecker(List<GameDeals>current, List<GameDeals>fetchedGames) {
    	Set<String> fetchedDealsIds = fetchedGames.stream().map(GameDeals::getDealID)
    			.collect(Collectors.toSet());
    	List<GameDeals> deletingDeals = current.stream().filter(deal -> 
    	!fetchedDealsIds.contains(deal.getDealID())).collect(Collectors.toList());
    	gameDealsRepository.deleteAll(deletingDeals);
    }
    
    @PostConstruct
    public void loadStores() {
    	String url = "https://www.cheapshark.com/api/1.0/stores";
    	ResponseEntity<Store[]> response = storeTemplate.getForEntity(url, Store[].class);
    	Store[] stores = response.getBody();
    	if (stores != null) {
   	        storeRepository.saveAll(Arrays.asList(stores));
            System.out.println("Loaded " + stores.length + " stores into DB.");
    	}
    }

}
