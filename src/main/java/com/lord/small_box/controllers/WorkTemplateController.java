package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.WorkTemplateDestinationDto;
import com.lord.small_box.dtos.WorkTemplateDto;
import com.lord.small_box.mappers.WorkTemplateMapper;
import com.lord.small_box.models.WorkTemplate;
import com.lord.small_box.services.WorkTemplateDestinationService;
import com.lord.small_box.services.WorkTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/work-templates")
@RequiredArgsConstructor
public class WorkTemplateController {
	
	@Autowired
	private final WorkTemplateService workTemplateService;
	
	@Autowired
	private final WorkTemplateDestinationService workTemplateDestinationService;
	
	private static final Gson gson = new Gson();
	
	@PostMapping("/create")
	ResponseEntity< WorkTemplateDto> createWorkTemplate(@RequestBody WorkTemplateDto workTemplateDto){
		WorkTemplate workTemplate = WorkTemplateMapper.INSTANCE.toWorkTemplate(workTemplateDto);
		WorkTemplate savedWorktemplate = workTemplateService.createTemplate(workTemplate);
		WorkTemplateDto savedWorkTemplateDto = WorkTemplateMapper.INSTANCE.toWorkTemplateDto(savedWorktemplate);
		return new ResponseEntity<WorkTemplateDto>(savedWorkTemplateDto,HttpStatus.CREATED);
	}

	@GetMapping("/by-id/{id}")
	ResponseEntity<WorkTemplateDto> findWorkTemplateById(@PathVariable("id")Long id){
		WorkTemplate template = workTemplateService.findWorkTemplateById(id);
		WorkTemplateDto templateDto = WorkTemplateMapper.INSTANCE.toWorkTemplateDto(template);
		return ResponseEntity.ok(templateDto);
	}
	
	@GetMapping("/by-organization-id/{id}")
	ResponseEntity<List<WorkTemplateDto>> findWorkTemplatesByOrganizationId(@PathVariable("id")Long id){
		List<WorkTemplate> templates = workTemplateService.findAllWorkTemplatesByOrganization(id);
		List<WorkTemplateDto> templatesDto = WorkTemplateMapper.INSTANCE.toWorkTemplateDtoList(templates);
		return ResponseEntity.ok(templatesDto);
		
	}
	
	@GetMapping("/by-user-id/{userId}")
	ResponseEntity<List<WorkTemplateDto>> findWorkTemplatesByuserId(@PathVariable("userId")Long userId){
		List<WorkTemplate> templates = workTemplateService.findAllWorkTemplatesByOrganizationByUserId(userId);
		List<WorkTemplateDto> templatesDto = WorkTemplateMapper.INSTANCE.toWorkTemplateDtoList(templates);
		return ResponseEntity.ok(templatesDto);
	}
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
	@DeleteMapping("/delete-work-template-by-id/{id}")
	ResponseEntity<String> deleteWorkTemplatebyId(@PathVariable("id")Long id){
		String result = workTemplateService.deleteWorkTemplateById(id);
		return ResponseEntity.ok(gson.toJson(result));
	}
	
	
	

}
