package com.example.GameDeal.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	 @Column(nullable = false, unique = true)
	private String email;
	 @Column(nullable = false, unique = true)
	private String username;
	 @Column(nullable = false)
	private String password;
	 @Column(nullable = false)
	public String profilePic = "/img/defaultProfilePic.png";
	
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
    public String getProfilepicture() {return profilePic;}
    
    public List<String> getFollowedGames() { return followedGames; }

    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFollowedGames(List<String> followedGames) { this.followedGames = followedGames;}
    public void setProfilePicture(String profilePic) {this.profilePic = profilePic;}
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){return Collections.emptyList();}
    
    @Override
    public boolean isAccountNonExpired() {return true;}
    
    @Override public boolean isAccountNonLocked() {return true;}
	@Override public boolean isCredentialsNonExpired() {return true;}
	@Override public boolean isEnabled() {return true;}
	
}