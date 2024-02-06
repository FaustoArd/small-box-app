package com.lord.small_box.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.WorkTemplateDto;
import com.lord.small_box.mappers.WorkTemplateMapper;
import com.lord.small_box.models.WorkTemplate;
import com.lord.small_box.services.WorkTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/work-templates")
@RequiredArgsConstructor
public class WorkTemplateController {
	
	private final WorkTemplateService workTemplateService;
	
	@PostMapping("/create")
	ResponseEntity< WorkTemplateDto> createWorkTemplate(@RequestBody WorkTemplateDto workTemplateDto){
		WorkTemplate workTemplate = WorkTemplateMapper.INSTANCE.toWorkTemplate(workTemplateDto);
		WorkTemplate savedWorktemplate = workTemplateService.createTemplate(workTemplate);
		WorkTemplateDto savedWorkTemplateDto = WorkTemplateMapper.INSTANCE.toWorkTemplateDto(savedWorktemplate);
		return new ResponseEntity<WorkTemplateDto>(savedWorkTemplateDto,HttpStatus.CREATED);
	}

	@GetMapping("/by_id/{id}")
	ResponseEntity<WorkTemplateDto> findWorkTemplateById(@PathVariable("id")Long id){
		WorkTemplate template = workTemplateService.findWorkTemplateById(id);
		WorkTemplateDto templateDto = WorkTemplateMapper.INSTANCE.toWorkTemplateDto(template);
		return ResponseEntity.ok(templateDto);
	}
	
	

}
