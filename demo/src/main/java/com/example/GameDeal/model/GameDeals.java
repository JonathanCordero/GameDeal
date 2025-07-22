package com.example.GameDeal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "game_deals")  
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDeals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String title;
    private double normalPrice;
    private double salePrice;
    private String thumb;
    @Column(unique = true)
    private String dealID;
    private String storeID;
    private String gameID;
    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "storeID")
    private Store store;
   
    public GameDeals() {}

    
    public GameDeals(String title, double normalPrice, double salePrice, String thumb, String storeID) {
        this.title = title;
        this.normalPrice = normalPrice;
        this.salePrice = salePrice;
        this.thumb = thumb;
        this.storeID=storeID;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

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
    
    public Store getStore() {return store;}
    public void setStore(Store store) {this.store=store;}
}
