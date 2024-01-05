package com.lord.small_box.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.AppUserLoginDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.TokenResponseDto;
import com.lord.small_box.services.AuthorizationService;

@RestController
@RequestMapping("/api/v1/small-box/authorization")
public class AuthorizationController {
	
	@Autowired
	private final AuthorizationService authorizationService;
	
	public AuthorizationController(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	@PostMapping("/register")
	ResponseEntity<AppUserRegistrationDto> register(@RequestBody AppUserRegistrationDto userDto,@RequestParam("authority")String authority){
		AppUserRegistrationDto registeredUserDto = authorizationService.register(userDto, authority);
		return new ResponseEntity<AppUserRegistrationDto>(registeredUserDto,HttpStatus.CREATED);

}
	@PostMapping("/login")
	ResponseEntity<TokenResponseDto> login(@RequestBody AppUserLoginDto appUserLoginDto){
		TokenResponseDto response = authorizationService.login(appUserLoginDto);
		return new ResponseEntity<TokenResponseDto>(response,HttpStatus.OK);
	}
}