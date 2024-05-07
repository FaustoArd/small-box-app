package com.lord.small_box.services;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.lord.small_box.dtos.AppUserDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.AppUserUpdatedDto;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Authority;

public interface AppUserService {
	
	public AppUser findByUsername(String username);
	
	public AppUser findById(Long id);
	
	public AppUser save(AppUser appUser);
	
	public boolean checkUsername(String username);
	
	public boolean checkUsernameUpdated(String username,long userId);
	
	public List<AppUserDto> findAll();
	
	public List<AppUserDto> findAllWithAuthorities();
	
	public AppUser mapUpdateDtoToUser(AppUserUpdatedDto appUserUpdatedDto,Set<Authority> roles,PasswordEncoder passwordEncoder);
	
	public String deleteUserById(long userId);

}
