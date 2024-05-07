package com.lord.small_box.services;

import org.springframework.security.core.AuthenticationException;

import com.lord.small_box.dtos.AppUserLoginDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.AppUserUpdatedDto;
import com.lord.small_box.dtos.LoginResponseDto;
import com.lord.small_box.models.AppUser;

public interface AuthorizationService {
	
	public AppUserRegistrationDto register(AppUserRegistrationDto appUserRegistrationDto,String authority);
	
	public LoginResponseDto login(AppUserLoginDto appUserLoginDto) throws AuthenticationException;
	
	public AppUserRegistrationDto updateUser(AppUserUpdatedDto appUserUpdatedDto,String authority);
	
	public boolean validatePassword(String password);

}
