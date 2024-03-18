package com.lord.small_box.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.AppUserLoginDto;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.LoginResponseDto;
import com.lord.small_box.services.AuthorizationService;

@RestController
@RequestMapping("/api/v1/smallbox/authorization")
public class AuthorizationController {
	
	@Autowired
	private final AuthorizationService authorizationService;
	
	private final Gson gson = new Gson();
	
	public AuthorizationController(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	@PostMapping("/login")
	ResponseEntity<LoginResponseDto> login(@RequestBody AppUserLoginDto loginDto){
		LoginResponseDto response = authorizationService.login(loginDto);
		return new ResponseEntity<LoginResponseDto>(response,HttpStatus.OK);
	}
	
	
}