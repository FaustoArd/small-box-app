package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.services.DepositRecevierService;
import com.lord.small_box.services.DepositRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-receiver")
@RequiredArgsConstructor
public class DepositReceiverController {
	@Autowired
	private final DepositRecevierService depositRecevierService;
	
	private static final Gson gson = new Gson();
	
	@GetMapping(path="/find-receivers-by-organization")
	ResponseEntity<List<DepositReceiverDto>> findAllReceiversByOrganization(@RequestParam("organizationId")long organizationId){
		List<DepositReceiverDto> receiverDtos = depositRecevierService.findAllReceiversByOrganization(organizationId);
		return ResponseEntity.ok(receiverDtos);
	}
}
