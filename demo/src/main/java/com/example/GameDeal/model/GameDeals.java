package com.example.GameDeal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "game_deals")  
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDeals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String title;
    private String store;
    private double normalPrice;
    private double salePrice;
    private String thumb;
    private String dealID;
    private String storeID;
    private String gameID;

   
    public GameDeals() {}

    
    public GameDeals(String title, String store, double normalPrice, double salePrice, String thumb) {
        this.title = title;
        this.store = store;
        this.normalPrice = normalPrice;
        this.salePrice = salePrice;
        this.thumb = thumb;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }

    public double getNormalPrice() { return normalPrice; }
    public void setNormalPrice(double normalPrice) { this.normalPrice = normalPrice; }

    public double getSalePrice() { return salePrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }

    public String getThumb() { return thumb; }
    public void setThumb(String thumb) { this.thumb = thumb; }
    
    public String getDealID() { return dealID; }
    public void setDealID(String dealID) { this.dealID = dealID; }

    public String getStoreID() { return storeID; }
    public void setStoreID(String storeID) { this.storeID = storeID; }

    public String getGameID() { return gameID; }
    public void setGameID(String gameID) { this.gameID = gameID; }
}
