package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.SmallBoxTypeDto;
import com.lord.small_box.mappers.ContainerMapper;
import com.lord.small_box.mappers.SmallBoxTypeMapper;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.services.ContainerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/containers")
@RequiredArgsConstructor
public class ContainerController {
	
	@Autowired
	private final ContainerService containerService;
	
	private final SmallBoxTypeRepository smallBoxTypeRepository;
	
	private final static Gson gson = new Gson();
	
	
	@GetMapping("/all-types")
	ResponseEntity<List<SmallBoxTypeDto>> findAllSmallBoxTypes(){
		List<SmallBoxType> types = smallBoxTypeRepository.findAll();
		List<SmallBoxTypeDto> typesDto = SmallBoxTypeMapper.INSTANCE.toSmallBoxesDto(types);
		return new ResponseEntity<List<SmallBoxTypeDto>>(typesDto,HttpStatus.OK);
	}
	
	@GetMapping("/all")
	ResponseEntity<List<ContainerDto>> findAllContainers(){
		List<Container> containers = containerService.findAll();
		List<ContainerDto> containersDto = ContainerMapper.INSTANCE.toContainersDto(containers);
		return new ResponseEntity<List<ContainerDto>>(containersDto,HttpStatus.OK);
	}
	
	@PostMapping("/")
	ResponseEntity<ContainerDto> createContainer(@RequestBody ContainerDto containerDto){
		
		Container container = ContainerMapper.INSTANCE.toContainer(containerDto);
		Container savedContainer = containerService.save(container);
		ContainerDto savedContainerDto = ContainerMapper.INSTANCE.toContainerDto(savedContainer);
		return new ResponseEntity<ContainerDto>(savedContainerDto,HttpStatus.CREATED);
		
	}
	@GetMapping("/{id}")
	ResponseEntity<ContainerDto> findContainerById(@PathVariable("id")Long id){
		Container container = containerService.findById(id);
		ContainerDto containerDto = ContainerMapper.INSTANCE.toContainerDto(container);
		return new ResponseEntity<ContainerDto>(containerDto,HttpStatus.OK);
	}
	@PutMapping("/set-total-write")
	ResponseEntity<String> setContainerTotalWrite(@RequestParam("containerId")Long containerId,@RequestParam("totalWrite")String totalWrite){
		containerService.setContainerTotalWrite(containerId, totalWrite);
		return ResponseEntity.ok(gson.toJson("Guardado!"));
	}
	@GetMapping("/all-by-organizations-by-user")
	ResponseEntity<List<ContainerDto>> findAllbyOrganizations(@RequestParam("userId")Long userId){
		List<ContainerDto> containersDto = containerService.findAllbyOrganizationsByUser(userId);
		return ResponseEntity.ok(containersDto);
	}
	

}
