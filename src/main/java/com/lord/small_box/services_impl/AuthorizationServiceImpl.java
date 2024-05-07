package com.lord.small_box.services_impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lord.small_box.dtos.AppUserLoginDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.AppUserUpdatedDto;
import com.lord.small_box.dtos.LoginResponseDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.exceptions.LoginException;
import com.lord.small_box.exceptions.PasswordInvalidException;
import com.lord.small_box.exceptions.UsernameExistException;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;
import com.lord.small_box.repositories.AuthorityRepository;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.AuthorizationService;
import com.lord.small_box.services.JwtTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

	@Autowired
	private final AppUserService appUserService;

	@Autowired
	private final PasswordEncoder passwordEncoder;

	@Autowired
	private final AuthenticationManager authenticationManager;

	@Autowired
	private final AuthorityRepository authorityRepository;

	@Autowired
	private final JwtTokenService tokenService;

	private static final Logger log = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

	@Override
	public AppUserRegistrationDto register(AppUserRegistrationDto userDto, String authority) {
		log.info("Register user");
		if (appUserService.checkUsername(userDto.getUsername())) {
			log.warn("username already exist");
			throw new UsernameExistException("El nombre de usuario ya existe");
		}
		if (!validatePassword(userDto.getPassword())) {
			log.warn("Invalid password");
			throw new PasswordInvalidException("Password invalido, lea los requisitos");
		} else {
			log.info("looking for role");
			Authority role = findByAuthority(authority);
			Set<Authority> roles = new HashSet<>();
			roles.add(role);
			log.info("saving user");
			AppUser user = new AppUser(userDto.getName(), userDto.getLastname(), userDto.getUsername(),
					userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), true, true, true, true, roles);
			AppUser registeredUser = appUserService.save(user);

			return mapUserToDto(registeredUser);
		}

	}
	@Override
	public AppUserRegistrationDto updateUser(AppUserUpdatedDto appUserUpdatedDto, String authority) {
		log.info("Update user");
		if (!validatePassword(appUserUpdatedDto.getPassword())) {
			log.warn("Invalid password");
			throw new PasswordInvalidException("Password invalido, lea los requisitos");
		} else {
			log.info("looking for role");
			Authority role = findByAuthority(authority);
			Set<Authority> roles = new HashSet<>();
			roles.add(role);
			log.info("saving user");
			AppUser updatedUser = appUserService.save(appUserService.mapUpdateDtoToUser(appUserUpdatedDto, roles, passwordEncoder));

			return mapUserToDto(updatedUser);
		}

	}
	
	@Override
	public LoginResponseDto login(AppUserLoginDto appUserLoginDto) throws AuthenticationException {
		log.info("Login user");
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					appUserLoginDto.getUsername(), appUserLoginDto.getPassword()));
			String token = tokenService.generateJwt(auth);
			LoginResponseDto loginResponseDto = new LoginResponseDto();
			AppUser loggedUser = appUserService.findByUsername(appUserLoginDto.getUsername());
			loginResponseDto.serUsername(loggedUser.getUsername());
			loginResponseDto.setToken(token);
			loginResponseDto.setUserId(loggedUser.getId());
			return loginResponseDto;

		} catch (AuthenticationException ex) {
			throw new LoginException("Usuario o contraseña invalido");
		}
	}

	//This method map user registered name and last name values to AppUserRegistrationDto to show in frontEnd
	private static AppUserRegistrationDto mapUserToDto(AppUser user) {
		if (user == null) {
			return null;
		}
		AppUserRegistrationDto userDto = new AppUserRegistrationDto();
		userDto.setName(user.getName());
		userDto.setLastname(user.getLastname());
		userDto.setUsername(null);
		userDto.setEmail(null);
		userDto.setPassword(null);
		return userDto;
	}

	@Override
	public boolean validatePassword(String password) {
		return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?![@#$%^&+=])(?=\\S+$).{8,}$", password);
	}
	
	private Authority findByAuthority(String authority) {
		return authorityRepository.findByAuthority(AuthorityName.valueOf(authority))
		.orElseThrow(() -> new ItemNotFoundException("No se encontro el rol"));
	} 

	

}
