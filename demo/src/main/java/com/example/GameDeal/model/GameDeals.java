package com.example.GameDeal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  // Marks this as a database entity
@Table(name = "game_deals")  // Specifies table name
public class GameDeals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    private String title;
    private String store;
    private double normalPrice;
    private double salePrice;
    private String thumb;

    // Default constructor
    public GameDeals() {}

    // Constructor for easy object creation
    public GameDeals(String title, String store, double normalPrice, double salePrice, String thumb) {
        this.title = title;
        this.store = store;
        this.normalPrice = normalPrice;
        this.salePrice = salePrice;
        this.thumb = thumb;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }

    public double getnormalPrice() { return normalPrice; }
    public void setnormalPrice(double normalPrice) { this.normalPrice = normalPrice; }

    public double getsalePrice() { return salePrice; }
    public void setsalePrice(double salePrice) { this.salePrice = salePrice; }

    public String getthumb() { return thumb; }
    public void setthumb(String thumb) { this.thumb = thumb; }
}
