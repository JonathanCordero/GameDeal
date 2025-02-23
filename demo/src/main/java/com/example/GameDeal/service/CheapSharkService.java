package com.example.GameDeal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.GameDeal.model.GameDeals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class CheapSharkService {
    private final String CHEAPSHARK_API_URL = "https://www.cheapshark.com/api/1.0/deals";

    public List<GameDeals> getGameDeals() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GameDeals[]> response = restTemplate.exchange(CHEAPSHARK_API_URL,HttpMethod.GET,null,
        		new ParameterizedTypeReference<GameDeals[]>() {});

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            // Handle error appropriately, maybe log it
            return Collections.emptyList(); // Return an empty list on failure
        }
    }

}
