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
	
	@Autowired
	private final OrganizationService organizationService;
	
	@PostMapping("/register")
	ResponseEntity<AppUserRegistrationDto> register(@RequestBody AppUserRegistrationDto userDto,@RequestParam("authority")String authority){
		AppUserRegistrationDto registeredUserDto = authorizationService.register(userDto, authority);
		return new ResponseEntity<AppUserRegistrationDto>(registeredUserDto,HttpStatus.CREATED);
	}
	
	@PutMapping("/add-organization")
	ResponseEntity<?> addOrganizationToUser(@RequestParam("userId")Long userId, @RequestParam("organizations")List<Long> organizationsId){
		String result = organizationService.addOrganizationToUser(userId, organizationsId);
		return new ResponseEntity<String>(result,HttpStatus.OK);
	}
}
