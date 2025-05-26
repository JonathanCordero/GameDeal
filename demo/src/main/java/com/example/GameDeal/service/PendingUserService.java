package com.example.GameDeal.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.GameDeal.model.User;
import com.example.GameDeal.model.PendingUser;

@Service
public class PendingUserService {

	private final Map<String, PendingUser> tokenForUser = new ConcurrentHashMap<>();
	private static final long Expiration_Time_Millis = 10 * 60 * 1000;
	
	public String savePendingUser(User user) {
		String token = UUID.randomUUID().toString();
		tokenForUser.put(token, new PendingUser(user));
		return token;
	}
	
	public User confirmUser(String token) {
		PendingUser pendingUser = tokenForUser.remove(token);
		if (pendingUser != null && !pendingUser.isExpired(Expiration_Time_Millis)) return pendingUser.getUser();
		return null;
	}
	
	public boolean tokenExists(String token) {
		PendingUser pendingUser = tokenForUser.get(token);
		if (pendingUser == null) return false;
		if (pendingUser.isExpired(Expiration_Time_Millis)) {
			tokenForUser.remove(token);
			return false;
		}
		return true;
	}
	
	@Scheduled(fixedRate = 60_000)
	public void cleanExpiredTokens() {
		tokenForUser.entrySet().removeIf(pendingUser -> pendingUser.getValue().isExpired(Expiration_Time_Millis));
	    /*int before = tokenForUser.size();
		tokenForUser.entrySet().removeIf(entry -> entry.getValue().isExpired(Expiration_Time_Millis));
		int after = tokenForUser.size();
		System.out.println("Cleaned " + (before - after) + " expired tokens.");*/
	}
}
