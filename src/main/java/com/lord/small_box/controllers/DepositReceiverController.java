package com.lord.small_box.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.DepositControlReceiverDto;
import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.services.DepositRecevierService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-receiver")
@RequiredArgsConstructor
public class DepositReceiverController {
	@Autowired
	private final DepositRecevierService depositRecevierService;
	

	
	@GetMapping(path="/find-receivers-by-organization")
	ResponseEntity<List<DepositReceiverDto>> findAllReceiversByOrganization(@RequestParam("organizationId")long organizationId){
		List<DepositReceiverDto> receiverDtos = depositRecevierService.findAllReceiversByOrganization(organizationId);
		return ResponseEntity.ok(receiverDtos);
	}
	@GetMapping(path = "/mark-as-readed")
	ResponseEntity<Boolean> markAsReaded(@RequestParam("depositReceiverId")long  depositReceiverId){
		boolean readed = depositRecevierService.markAsReaded(depositReceiverId);
		return ResponseEntity.ok(readed);
	}
	@GetMapping(path = "/count-messages")
	ResponseEntity<Long> countMessages(@RequestParam("organizationId")long organizationId){
		long messageQuantity = depositRecevierService.countMessages(organizationId);
		return ResponseEntity.ok(messageQuantity);
	}
	@GetMapping(path="/find-all-control-receivers-by-receiver")
	ResponseEntity<List<DepositControlReceiverDto>> findAllControlReceiversByReceiver(
			@RequestParam("depositReceiverId")long depositReceiverId){
		List<DepositControlReceiverDto> receiverDtos = depositRecevierService.findAllByDepositReceiver(depositReceiverId);
		return ResponseEntity.ok(receiverDtos);
	}
	@DeleteMapping(path="/delete-deposit-receiver/{depositReceiverId}")
	ResponseEntity<String> deleteControlRequestbyId(@PathVariable("depositReceiverId")long depositReceiverId){
		String deletedReceiverCode = depositRecevierService.deleteDepositReceiver(depositReceiverId);
		return ResponseEntity.ok(deletedReceiverCode);
	}
}
