package com.example.GameDeal.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class StoreImages {
    private String banner;
    private String logo;
    private String icon;

    public StoreImages() {}

    public String getBanner() { return banner; }
    public String getLogo() { return logo; } 
    public String getIcon() { return icon; }
    
    public void setBanner(String banner) { this.banner = banner; }
    public void setLogo(String logo) { this.logo = logo; }
    public void setIcon(String icon) { this.icon = icon; }
}
