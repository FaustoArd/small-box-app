package com.lord.small_box.services;

import org.springframework.security.core.AuthenticationException;

import com.lord.small_box.dtos.AppUserLoginDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.TokenResponseDto;
import com.lord.small_box.models.AppUser;

public interface AuthorizationService {
	
	public AppUserRegistrationDto register(AppUserRegistrationDto appUserRegistrationDto,String authority);
	
	public TokenResponseDto login(AppUserLoginDto appUserLoginDto) throws AuthenticationException;
	
	

}
