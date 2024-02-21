package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.LocationContractDto;
import com.lord.small_box.mappers.LocationContractMapper;
import com.lord.small_box.models.LocationContract;
import com.lord.small_box.services.LocationContractService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/location-contracts")
@RequiredArgsConstructor
public class LocationContractController {
	
	@Autowired
	private final LocationContractService locationContractService;
	
	

	@PostMapping("/create")
	ResponseEntity<LocationContractDto> createLocationContract(@RequestBody LocationContractDto locationContractDto){
		LocationContract locationContract = locationContractService.createLocationContract(locationContractDto);
		LocationContractDto createdLocationContractDto = LocationContractMapper.INSTANCE.locationContractToDto(locationContract);
		return new ResponseEntity<LocationContractDto>(createdLocationContractDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/by_id/{id}")
	ResponseEntity<LocationContractDto> findLocationContractById(@PathVariable("id")Long id){
		LocationContract locationContract = locationContractService.findLocationContractById(id);
		LocationContractDto locationContractDto = LocationContractMapper.INSTANCE.locationContractToDto(locationContract);
		return ResponseEntity.ok(locationContractDto);
	}
	
	@GetMapping("/find_all_by_org_by_user_id")
	ResponseEntity<List<LocationContractDto>> findAllLocationContractsByUserId(@RequestParam("userId")Long userId){
		List<LocationContract> contracts = locationContractService.findAllLocationContractByOrganizationByUserId(userId);
		List<LocationContractDto> contractsDto = LocationContractMapper.INSTANCE.locationContractsToDtos(contracts);
		return ResponseEntity.ok(contractsDto);
	}
}
