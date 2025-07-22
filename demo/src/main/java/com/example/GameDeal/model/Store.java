package com.example.GameDeal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stores")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Store {

	@Id
	@Column(name = "storeid")
	private String storeID;
	private String storeName;
	@Embedded
	private StoreImages images;
	
	public Store() {
		
	}
	
	public Store(String storeID, String storeName, StoreImages images) {
		this.storeID = storeID;
		this.storeName = storeName;
		this.images = images;
	}
	
	public String getStoreID() {return storeID;}
	public String getStoreName() {return storeName;}
	public StoreImages getImages() {return images;}
	
	public void setStoreID(String storeID) {this.storeID=storeID;}
	public void setStoreName(String storeName) {this.storeName=storeName;}
	public void setImages(StoreImages images) {this.images = images;}
}
