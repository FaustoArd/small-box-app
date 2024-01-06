package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.AppUserDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.models.AppUser;

public interface AppUserService {
	
	public AppUser findByUsername(String username);
	
	public AppUser findById(Long id);
	
	public AppUser save(AppUser appUser);
	
	public boolean checkUsername(String username);
	
	public List<AppUserDto> findAll();

}
