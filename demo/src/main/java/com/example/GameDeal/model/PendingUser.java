package com.example.GameDeal.model;

public class PendingUser {

	private final User user;
	private long creationTime;
	
	public PendingUser(User user) {
		this.user = user;
		this.creationTime = System.currentTimeMillis();
	}
	
	public User getUser() {
		return this.user;
	}
	
	public boolean isExpired(long maxAge) {
		return System.currentTimeMillis()- this.creationTime > maxAge;
	}
}
