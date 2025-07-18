package com.example.GameDeal.model;

public class PendingEmailChange {

	private final Long userId;
	private final String newEmail;
	private long creationTime;
	
	public PendingEmailChange(Long userId, String newEmail) {
		this.userId=userId;
		this.newEmail=newEmail;
		this.creationTime=System.currentTimeMillis();
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public String getNewEmail() {
		return newEmail;
	}
	public boolean isExpired(long maxAge) {
		return System.currentTimeMillis()-this.creationTime>maxAge;
	}
}
