package com.lord.small_box.services_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.repositories.AppUserRepository;
import com.lord.small_box.services.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService,UserDetailsService {

	@Autowired
	private final AppUserRepository appUserRepository;
	
	private static final String userNotFound = "No se encontro el usuario";
	
	public AppUserServiceImpl(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return findByUsername(username);
	}

	@Override
	public AppUser findByUsername(String username) {
		return appUserRepository.findByUsername(username).orElseThrow(()-> new ItemNotFoundException(userNotFound));
	}

	@Override
	public AppUser findById(Long id) {
		return appUserRepository.findById(id).orElseThrow(()-> new ItemNotFoundException(userNotFound));
	}

	@Override
	public AppUser save(AppUser appUser) {
		return appUserRepository.save(appUser);
	}

	@Override
	public boolean checkUsername(String username) {
		return appUserRepository.findByUsername(username).isPresent();
	}
	
	

}
