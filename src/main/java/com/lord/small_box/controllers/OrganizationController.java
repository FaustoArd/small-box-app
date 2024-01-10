package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.OrganizationResponsibleDto;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.OrganizationResponsibleService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/organization")
@RequiredArgsConstructor
public class OrganizationController {
	
	@Autowired
	private final OrganizationService organizationService;
	
	@Autowired
	private final OrganizationResponsibleService organizationResponsibleService;
	
	
	
	private static final Gson gson = new Gson();
	
	@GetMapping("/all-orgs")
	ResponseEntity<List<OrganizationDto>> findAllOrganizations(){
		List<OrganizationDto> orgsDto = organizationService.findAll();
		return ResponseEntity.ok(orgsDto);
	}
	@GetMapping("/all-orgs-by-id")
	ResponseEntity<List<OrganizationDto>> findAllOrganizationsById(@RequestParam("organizationsId")List<Long> organizationsId){
		List<OrganizationDto> orgsDto = organizationService.findAllById(organizationsId);
		return ResponseEntity.ok(orgsDto);
	}
	
	@PutMapping("/add-organization")
	ResponseEntity<String> addOrganizationToUser(@RequestParam("userId")Long userId, @RequestParam("organizationsId")List<Long> organizationsId){
		String result = organizationService.addOrganizationToUser(userId, organizationsId);
		return new ResponseEntity<String>(gson.toJson(result),HttpStatus.OK);
	}
	@GetMapping("/all-orgs-by-user")
	ResponseEntity<List<OrganizationDto>> findOrganizationsByUser(@RequestParam("userId")Long userId){
		List<OrganizationDto> orgsDto = organizationService.findOrganizationByUser(userId);
		return ResponseEntity.ok(orgsDto);
	}
	@PostMapping("/new-organization")
	ResponseEntity<OrganizationDto> newOrganization(@RequestBody OrganizationDto organizationDto){
		return new ResponseEntity<OrganizationDto>(new OrganizationDto(),HttpStatus.NOT_IMPLEMENTED);
	}
	@PostMapping("/new-responsible")
	ResponseEntity<OrganizationResponsibleDto> newResponsible(@RequestBody OrganizationResponsibleDto organizationResponsibleDto){
		OrganizationResponsibleDto responseOrganizationResponsibleDto = organizationResponsibleService.save(organizationResponsibleDto);
		return new ResponseEntity<OrganizationResponsibleDto>(responseOrganizationResponsibleDto,HttpStatus.CREATED);
	}


}
