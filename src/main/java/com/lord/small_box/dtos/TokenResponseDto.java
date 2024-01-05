package com.lord.small_box.dtos;

public class TokenResponseDto {
	
	private String token;
	
	public TokenResponseDto(String token) {
		super();
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
