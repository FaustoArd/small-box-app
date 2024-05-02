package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.services.DepositRequestService;
import com.lord.small_box.services.PurchaseOrderService;
import com.lord.small_box.services.SupplyService;
import com.lord.small_box.utils.ExcelToListUtils;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-request")
@RequiredArgsConstructor
public class DepositRequestController {
	
	@Autowired
	private final DepositRequestService depositRequestService;
	
	private static final Gson gson = new Gson();
	
	@PostMapping(path="/create-request")
	ResponseEntity<DepositRequestDto> createRequest(@RequestBody DepositRequestDto depositRequestDto){
		DepositRequestDto savedRequestDto = depositRequestService.createRequest(depositRequestDto);
		return  new ResponseEntity<DepositRequestDto>(savedRequestDto,HttpStatus.CREATED);
	}
	
	@PostMapping(path="/save-items-to-request")
	ResponseEntity<DepositRequestDto> saveItemsToRequest(@RequestBody DepositRequestDto requestDto){
		DepositRequestDto savedRequestDto = depositRequestService.saveItemsToRequest(requestDto);
		return new ResponseEntity<DepositRequestDto>(savedRequestDto,HttpStatus.CREATED);
	}
	
	@PostMapping(path="/send-request")
	ResponseEntity<String> sendRequest(@RequestParam("depositRequestId")long depositRequestId
			,@RequestParam("destinationOrganizationId")long destinationOrganizationId){
		String requestCode = depositRequestService.sendRequest(depositRequestId, destinationOrganizationId);
		return new ResponseEntity<String>(gson.toJson(requestCode),HttpStatus.CREATED);
	}
	@GetMapping(path="/find-requests")
	ResponseEntity<List<DepositRequestDto>> findAllRequestsByOrganization(@RequestParam("userId")long userId){
		List<DepositRequestDto> requestDtos = depositRequestService.findAllRequestbyOrganizationByUserId(userId);
		return ResponseEntity.ok(requestDtos);
	}
	@GetMapping(path="/find-control-requests")
	ResponseEntity<List<DepositControlRequestDto>> findAllControlRequestsByRequest(@RequestParam("depositRequestId")long depositRequestId){
		List<DepositControlRequestDto> controlRequestsDto = depositRequestService.findAllRequestControlsByDepositRequest(depositRequestId);
		return ResponseEntity.ok(controlRequestsDto);
	}

}
