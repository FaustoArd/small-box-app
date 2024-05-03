package com.lord.small_box.services_impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.lord.small_box.dtos.AppUserDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.AppUserMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.repositories.AppUserRepository;
import com.lord.small_box.services.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService,UserDetailsService {

	@Autowired
	private final AppUserRepository appUserRepository;
	
	private static final String userNotFound = "No se encontro el usuario";
	
	private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);
	
	public AppUserServiceImpl(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Load user by username");
	return findByUsername(username);
	}

	@Override
	public AppUser findByUsername(String username) {
		log.info("find user by  username");
		return appUserRepository.findByUsername(username).orElseThrow(()-> new ItemNotFoundException(userNotFound));
	}

	@Override
	public AppUser findById(Long id) {
		log.info("find user by id");
		return appUserRepository.findById(id).orElseThrow(()-> new ItemNotFoundException(userNotFound));
	}

	@Override
	public AppUser save(AppUser appUser) {
		log.info("Save user");
		return appUserRepository.save(appUser);
	}

	@Override
	public boolean checkUsername(String username) {
		log.info("Checking existing username");
		return appUserRepository.findByUsername(username).isPresent();
	}

	@Override
	public List<AppUserDto> findAll() {
		log.info("Find all users");
		List<AppUser> users = appUserRepository.findAll();
		return AppUserMapper.INSTANCE.toUsersDto(users);
	}
	
	

}
