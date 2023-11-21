package com.lord.small_box.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.mappers.ContainerMapper;
import com.lord.small_box.models.Container;
import com.lord.small_box.repositories.ContainerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/containers")
@RequiredArgsConstructor
public class ContainerController {
	
	@Autowired
	private final ContainerRepository containerRepository;
	
	@PostMapping("/")
	ResponseEntity<ContainerDto> createContainer(@RequestBody ContainerDto containerDto){
		
		Container container = ContainerMapper.INSTANCE.toContainer(containerDto);
		Container savedContainer = containerRepository.save(container);
		ContainerDto savedContainerDto = ContainerMapper.INSTANCE.toContainerDto(savedContainer);
		return new ResponseEntity<ContainerDto>(savedContainerDto,HttpStatus.CREATED);
		
	}

}
