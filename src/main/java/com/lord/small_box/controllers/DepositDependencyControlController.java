package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.DepositDependencyControlDto;
import com.lord.small_box.dtos.UpdateDependencyItemReportDto;
import com.lord.small_box.models.DepositDependencyControl;
import com.lord.small_box.services.DepositDependencyControlService;

@RestController
@RequestMapping("/api/v1/smallbox/dependency-control")
public class DepositDependencyControlController {
	
	@Autowired
	private final DepositDependencyControlService dependencyControlService;

	public DepositDependencyControlController(DepositDependencyControlService dependencyControlService) {
		this.dependencyControlService = dependencyControlService;
	}
	@GetMapping(path="/find-dependency-control-by-id/{dependencyControlId}")
	ResponseEntity<DepositDependencyControlDto> findDependencyControlById(@PathVariable("dependencyControlId")long dependencyControlId){
		DepositDependencyControlDto dto = dependencyControlService.findDependencyControlById(dependencyControlId);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping(path="/find-dependency-controls-by-deposit")
	ResponseEntity<List<DepositDependencyControlDto>> findAllDependencyControlsByDeposit(@RequestParam("depositId")long depositId){
		List<DepositDependencyControlDto> dtos = dependencyControlService.findAllByDeposit(depositId);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping(path="/find-dependency-controls")
	ResponseEntity<List<DepositDependencyControlDto>> findAllDependencyControlsByOrganizationAndDeposit
	(@RequestParam("applicantOrganizationId") long applicantOrganizationId,@RequestParam("depositId")long depositId){
		List<DepositDependencyControlDto> dtos = dependencyControlService.findAllByApplicantOrganizationAndDeposit(applicantOrganizationId, depositId);
		return ResponseEntity.ok(dtos);
		
	}
	
	@GetMapping(path="/decrease-item-quantity")
	ResponseEntity<List<UpdateDependencyItemReportDto>> decreaseItemQuantity
	(@RequestParam("itemId")long itemId,@RequestParam("itemQuantity")int itemQuantity, @RequestParam("depositId")long depositId){
		List<UpdateDependencyItemReportDto> report = dependencyControlService.decreaseItemQuantity(itemId, itemQuantity, depositId);
		return new ResponseEntity<List<UpdateDependencyItemReportDto>>(report,HttpStatus.OK);
	}
	
}
