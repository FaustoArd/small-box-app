package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.services.AuthorizationService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/registration")
@RequiredArgsConstructor
public class RegistrationController {
	
	@Autowired
	private final AuthorizationService authorizationService;
	
	@PostMapping("/register")
	ResponseEntity<AppUserRegistrationDto> register(@RequestBody AppUserRegistrationDto userDto,@RequestParam("authority")String authority){
		AppUserRegistrationDto registeredUserDto = authorizationService.register(userDto, authority);
		return new ResponseEntity<AppUserRegistrationDto>(registeredUserDto,HttpStatus.CREATED);
	}
	
	
}
