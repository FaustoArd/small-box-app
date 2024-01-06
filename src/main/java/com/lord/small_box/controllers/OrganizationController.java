package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/organization")
@RequiredArgsConstructor
public class OrganizationController {
	
	private final OrganizationService organizationService;
	
	
	
	private static final Gson gson = new Gson();
	
	@GetMapping("/all-orgs")
	ResponseEntity<List<OrganizationDto>> findAllOrganizations(){
		List<OrganizationDto> orgsDto = organizationService.findAll();
		return ResponseEntity.ok(orgsDto);
	}
	
	@PutMapping("/add-organization")
	ResponseEntity<String> addOrganizationToUser(@RequestParam("userId")Long userId, @RequestParam("organizationsId")List<Long> organizationsId){
		String result = organizationService.addOrganizationToUser(userId, organizationsId);
		return new ResponseEntity<String>(gson.toJson(result),HttpStatus.OK);
	}

}
