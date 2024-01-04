package com.lord.small_box.services;

import org.springframework.security.core.Authentication;

public interface JwtTokenService {
	
	public String generateJwt(Authentication auth);
	

}
