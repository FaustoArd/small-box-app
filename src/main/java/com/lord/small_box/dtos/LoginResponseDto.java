package com.lord.small_box.dtos;

public class LoginResponseDto {
	
	private Long userId;
	
	private String username;
	
	private String token;
	
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void serUsername(String username) {
		this.username = username;
	}

}
