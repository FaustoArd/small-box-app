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
import com.lord.small_box.dtos.DispatchControlDto;
import com.lord.small_box.mappers.DispatchControlMapper;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.services.DispatchControlService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/dispatchs")
@RequiredArgsConstructor
public class DispatchControlController {
	
	@Autowired
	private final DispatchControlService dispatchControlService;
	
	private static final Gson gson = new Gson();
	
	@PostMapping("/create_dispatch")
	 ResponseEntity<String> createDispatch(@RequestBody DispatchControlDto dispatchControlDto){
		 DispatchControl dispatchControl = DispatchControlMapper.INSTANCE.dtoToDispatch(dispatchControlDto);
		 String result  = dispatchControlService.createDispatch(dispatchControl);
		 return new ResponseEntity<String>(gson.toJson(result),HttpStatus.OK);
	 }
	
	@DeleteMapping("/delete_dispatch/{id}")
	ResponseEntity<String> deleteDispatch(@PathVariable("id") Long id){
		String result = dispatchControlService.deleteById(id);
		return ResponseEntity.ok(gson.toJson(result));
	}
	
	@GetMapping("/find_dispatch/{id}")
	ResponseEntity<DispatchControlDto> findDistpachById(@PathVariable("id")Long id){
		DispatchControl dispatchControl = dispatchControlService.findById(id);
		DispatchControlDto dispatchControlDto = DispatchControlMapper.INSTANCE.dispatchToDto(dispatchControl);
		return ResponseEntity.ok(dispatchControlDto);
	}
	@GetMapping("/find_all_dispatch_by_org")
	ResponseEntity<List<DispatchControlDto>> findAllDispatchsByOrganizationId(@RequestParam("organizationId")Long organizationId){
		List<DispatchControl> dispatchControls = dispatchControlService.findAllDistpachControlsByOrganization(organizationId);
		List<DispatchControlDto> dispatchControlDtos = DispatchControlMapper.INSTANCE.dispatchsToDtos(dispatchControls);
		return ResponseEntity.ok(dispatchControlDtos);
	}
	
	@PostMapping("/dispatch_work_template")
	ResponseEntity<String> dispatchWorkTemplate(@RequestParam("workTeamplateId")Long workTemplateId){
		String dispatchResult = dispatchControlService.dispatchWorkTemplate(workTemplateId);
		return new ResponseEntity<String>(gson.toJson(dispatchResult),HttpStatus.OK);
	}
	

}
