package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.WorkTemplateDestinationDto;
import com.lord.small_box.services.WorkTemplateDestinationService;
import com.lord.small_box.services.WorkTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/template_destination")
@RequiredArgsConstructor
public class TemplateDestinationController {
	
	@Autowired
	private final WorkTemplateDestinationService workTemplateDestinationService;
	
	private static final Gson gson = new Gson();
	
	@GetMapping("/all-template-destinations")
	ResponseEntity<List<WorkTemplateDestinationDto>> findAllTemplateDestinations(){
		List<WorkTemplateDestinationDto> destinationsDto = workTemplateDestinationService.findAllDestinations();
		return ResponseEntity.ok(destinationsDto);
	}
	
	@PostMapping("/create-template-destination")
	ResponseEntity<String> createTemplateDestination(@RequestParam("destination")String destination){
		String strDestination = workTemplateDestinationService.createDestination(new WorkTemplateDestinationDto(destination));
		return new  ResponseEntity<String>(gson.toJson(strDestination),HttpStatus.CREATED);
		
	}
	@DeleteMapping("/delete-template-destination/{id}")
	ResponseEntity<String> deleteTemplateDestinationById(@PathVariable("id")Long id){
		String result = workTemplateDestinationService.deleteDestinationById(id);
		return  ResponseEntity.ok(gson.toJson(result));
	}

}
