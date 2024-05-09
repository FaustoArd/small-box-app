package com.lord.small_box.services_impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lord.small_box.dtos.AppUserDto;
import com.lord.small_box.dtos.AppUserUpdatedDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.AppUserMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Authority;
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

	@Override
	public List<AppUserDto> findAllWithAuthorities() {
		log.info("Find all users with authorities");
		List<AppUser> users= appUserRepository.findAll();
		List<AppUserDto> userDtos  =  mapUserToDtoWithAutorities(users);
		return userDtos;
	}
	
	private static List<AppUserDto> mapUserToDtoWithAutorities(List<AppUser> users){
		
		List<AppUserDto> userDtos = users.stream().map(user -> {
			AppUserDto userDto = AppUserMapper.INSTANCE.toUserDto(user);
			userDto.setAuthorities(user.getAuthorities().stream().map(auth -> auth.getAuthority()).toList());
			return userDto;
		}).toList();
		return userDtos;
	}

	@Override
	public AppUser mapUpdateDtoToUser(AppUserUpdatedDto appUserUpdatedDto, Set<Authority> roles,PasswordEncoder passwordEncoder) {
		return appUserRepository.findById(appUserUpdatedDto.getId()).map(user ->{
			user.setName(appUserUpdatedDto.getName());
			user.setLastname(appUserUpdatedDto.getLastname());
			user.setEmail(appUserUpdatedDto.getEmail());
			user.setPassword(passwordEncoder.encode(appUserUpdatedDto.getPassword()));
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);
			user.setEnabled(true);
			user.setAuhtorities(roles);
			return user;
		}).get();
	}

	@Override
	public String deleteUserById(long userId) {
		if(appUserRepository.existsById(userId)) {
			AppUser deletedUser = findById(userId);
			appUserRepository.deleteById(userId);
			return deletedUser.getName() + " " + deletedUser.getLastname();
		}
		throw new ItemNotFoundException(userNotFound);
	}

	
		
		 
		
	}
	
	
	
	


