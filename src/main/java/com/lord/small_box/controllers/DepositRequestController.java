package com.lord.small_box.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.services.DepositRequestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-request")
@RequiredArgsConstructor
public class DepositRequestController {
	
	@Autowired
	private final DepositRequestService depositRequestService;
	
	private static final Gson gson = new Gson();
	
	private static final Logger log = LoggerFactory.getLogger(DepositRequestController.class);
	
	@PostMapping(path="/create-request")
	ResponseEntity<DepositRequestDto> createRequest(@RequestBody DepositRequestDto depositRequestDto){
		
		DepositRequestDto savedRequestDto = depositRequestService.createRequest(depositRequestDto);
		return  new ResponseEntity<DepositRequestDto>(savedRequestDto,HttpStatus.CREATED);
	}
	
	@PutMapping(path="/set-destination-organization")
	ResponseEntity<DepositRequestDto> setDestinationOrganization(@RequestBody DepositRequestDto depositRequestDto){
		log.info("Deposit request controller, Destination organization id value: "+depositRequestDto.getDestinationOrganizationId());
		DepositRequestDto updatedDepositRequestDto = depositRequestService.setDestinationOrganization(depositRequestDto);
		return new ResponseEntity<DepositRequestDto>(updatedDepositRequestDto,HttpStatus.OK);
	}
	
	@PostMapping(path="/save-items-to-request")
	ResponseEntity<DepositRequestDto> saveItemsToRequest(@RequestBody DepositRequestDto requestDto){
		DepositRequestDto savedRequestDto = depositRequestService.saveItemsToRequest(requestDto);
		return new ResponseEntity<DepositRequestDto>(savedRequestDto,HttpStatus.CREATED);
	}
	
	@PostMapping(path="/send-request")
	ResponseEntity<String> sendRequest(@RequestParam("depositRequestId")long depositRequestId){
		String requestCode = depositRequestService.sendRequest(depositRequestId);
		return new ResponseEntity<String>(gson.toJson(requestCode),HttpStatus.CREATED);
	}
	@GetMapping(path="/find-requests")
	ResponseEntity<List<DepositRequestDto>> findAllRequestsByOrganization(@RequestParam("userId")long userId){
		List<DepositRequestDto> requestDtos = depositRequestService.findAllRequestbyOrganizationByUserId(userId);
		return ResponseEntity.ok(requestDtos);
	}
	@GetMapping(path="/find-control-requests")
	ResponseEntity<List<DepositControlRequestDto>> findAllControlRequestsByRequest(@RequestParam("depositRequestId")long depositRequestId){
		List<DepositControlRequestDto> controlRequestsDto = depositRequestService.findAllControlRequestsByDepositRequest(depositRequestId);
		return ResponseEntity.ok(controlRequestsDto);
	}
	
	@DeleteMapping(path="/delete-deposit-request/{depositRequestId}")
	ResponseEntity<String> deleteControlRequestbyId(@PathVariable("depositRequestId")long depositRequestId){
		String deletedRequestCode = depositRequestService.deleteDepositRequest(depositRequestId);
		return ResponseEntity.ok(deletedRequestCode);
	}

}
