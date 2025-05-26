package com.example.GameDeal.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	 @Column(nullable = false, unique = true)
	private String email;
	 @Column(nullable = false, unique = true)
	private String username;
	 @Column(nullable = false)
	private String password;
	
	@ElementCollection
	private List<String> followedGames;
	
	public User() {}
	
	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public List<String> getFollowedGames() { return followedGames; }

    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFollowedGames(List<String> followedGames) { this.followedGames = followedGames; }
	
}
