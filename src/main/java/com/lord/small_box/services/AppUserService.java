package com.lord.small_box.services;

import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.models.AppUser;

public interface AppUserService {
	
	public AppUser findByUsername(String username);
	
	public AppUser findById(Long id);
	
	public AppUser save(AppUser appUser);

}
