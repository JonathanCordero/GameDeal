package com.example.GameDeal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.GameDeal.model.GameDeals;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class CheapSharkService {

    @Value("${cheapshark.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public CheapSharkService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GameDeals> getDeals() {
        // Call the API and fetch data
        String url = apiUrl + "/deals?limit=10";
        ResponseEntity<List<GameDeals>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GameDeals>>() {});
        return response.getBody();
    }
}
