package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-control")
@RequiredArgsConstructor
public class DepositControlController {

	@Autowired
	private final DepositControlService depositControlService;
	
	private static final Gson gson = new Gson();
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	@GetMapping(path="/find-deposit-controls-by-org")
	ResponseEntity<List<DepositControlDto>> findDepositControlsByDeposit(@RequestParam("depositId")long depositId){
	List<DepositControlDto> controlsDto = depositControlService.findDepositControlsByDeposit(depositId);
	return ResponseEntity.ok(controlsDto);
	}
	
	@GetMapping(path = "/find-all-orders-by-org")
	ResponseEntity<List<PurchaseOrderDto>> findAllOrdersByOrganization(@RequestParam("organizationId")long organizationId){
		List<PurchaseOrderDto> purchaseOrderDtos =depositControlService.findAllOrdersByOrganizationId(organizationId);
		return ResponseEntity.ok(purchaseOrderDtos);
	}
	@GetMapping(path="/find-order-items")
	ResponseEntity<List<PurchaseOrderItemDto>> findPurchaseOrderItems(@RequestParam("purchaseOrderId")long purchaseOrderId){
		List<PurchaseOrderItemDto> itemDtos = depositControlService.findPurchaseOrderItems(purchaseOrderId);
		return ResponseEntity.ok(itemDtos);
	}
	
	@GetMapping(path ="/find-all-supplies-by-org")
	ResponseEntity<List<SupplyDto>> findAllSuppliesByOrganization(@RequestParam("organizationId")long organizationId){
		List<SupplyDto> supplyDtos = depositControlService.findAllSuppliesByOrganizationId(organizationId);
		return ResponseEntity.ok(supplyDtos);
	}
	@GetMapping(path = "/find-supply-items")
	ResponseEntity<List<SupplyItemDto>> findSupplyItems(@RequestParam("supplyId")long supplyId){
		List<SupplyItemDto> itemDtos = depositControlService.findSupplyItems(supplyId);
		return ResponseEntity.ok(itemDtos);
	}

	@PostMapping(path = "/collect-supply-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<SupplyDto> collectSupplyFromText(@RequestPart("file") MultipartFile file,
			@RequestParam("organizationId") long organizationId) throws Exception {
		String text = pdfToStringUtils.pdfToString(file.getOriginalFilename());
		SupplyDto supplyDto = depositControlService.collectSupplyFromText(text,organizationId);
		return new ResponseEntity<SupplyDto>(supplyDto, HttpStatus.CREATED);
	}
	
	@PostMapping(path="/collect-purchase-order-pdf",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<PurchaseOrderDto> collectPurchaseOrderFromText(@RequestPart("file")MultipartFile file,
			@RequestParam("organizationId")long OrganizationId) throws Exception{
		String text = pdfToStringUtils.pdfToString(file.getOriginalFilename());
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, OrganizationId);
		return new ResponseEntity<PurchaseOrderDto>(purchaseOrderDto,HttpStatus.CREATED);
	}
	
	//Find purchase order, with items.
	@GetMapping(path ="/find-full-purchase-order/{purchaseOrderId}")
	ResponseEntity<PurchaseOrderDto> findFullPurchaseOrder(@PathVariable("purchaseOrderId")long purchaseOrderId){
		PurchaseOrderDto purchaseOrderDto = depositControlService.findFullPurchaseOrder(purchaseOrderId);
		return ResponseEntity.ok(purchaseOrderDto);
	}
	
	@PutMapping(path = "/load-order-to-deposit")
	ResponseEntity<List<PurchaseOrderToDepositReportDto>> loadPurchaseOrdertoDeposit
	(@RequestBody long purchaseOrderId,@RequestParam("depositId")long depositId){
		List<PurchaseOrderToDepositReportDto> loadReport = depositControlService.loadPurchaseOrderToDepositControl(purchaseOrderId,depositId);
		return new ResponseEntity<List<PurchaseOrderToDepositReportDto>>(loadReport,HttpStatus.OK);
	}
	
	@GetMapping(path = "/create-supply-report")
	ResponseEntity<List<SupplyReportDto>> createSupplyResport
	(@RequestParam("supplyId")long supplyId,@RequestParam("depositId")long depositId){
		List<SupplyReportDto> report = depositControlService.createSupplyReport(supplyId,depositId);
		return ResponseEntity.ok(report);
	}
	
	@GetMapping(path = "/create-supply-correction-note")
	ResponseEntity<SupplyCorrectionNoteDto> createSupplyCorrectionNote
	(@RequestParam("supplyId")long supplyId,@RequestParam("depositId")long depositId){
		SupplyCorrectionNoteDto supplyCorrectionNote = depositControlService.createSupplyCorrectionNote(supplyId,depositId);
		return ResponseEntity.ok(supplyCorrectionNote);
	}
	
	@PostMapping(path="/create-deposit")
	ResponseEntity<String> createDeposit(@RequestBody DepositDto depositDto){
		String depositName = depositControlService.createDeposit(depositDto);
		return new ResponseEntity<String>(gson.toJson(depositName),HttpStatus.CREATED);
	}
	@GetMapping(path="/find-deposits")
	ResponseEntity<List<DepositDto>> findDeposits(@RequestParam("organizationId")long organizationId){
		List<DepositDto> depositsDto = depositControlService.findAllDepositsbyOrganization(organizationId);
		return ResponseEntity.ok(depositsDto);
	}
	@PutMapping(path="/set-current-deposit")
	ResponseEntity<DepositResponseDto> setCurrentDepositId(
			@RequestParam("userId")long userId,@RequestParam("organizationId")long organizationId,@RequestBody long depositId){
		DepositResponseDto depositResponse = depositControlService.setCurrentDeposit(userId,organizationId, depositId);
		return new ResponseEntity<DepositResponseDto>(depositResponse,HttpStatus.OK);
	}
	@GetMapping(path="/get-current-deposit")
	ResponseEntity<DepositResponseDto> getCurrentDepositId
	(@RequestParam("userId")long userId,@RequestParam("organizationId")long organizationId){
		DepositResponseDto depositResponse = depositControlService.getCurrentDepositId(userId,organizationId);
		return ResponseEntity.ok(depositResponse);
	}
	@DeleteMapping(path="/delete-purchase-order/{orderId}")
	ResponseEntity<Integer> deletePurchaseOrder(@PathVariable("orderId")long orderId){
		int orderNumberDeleted = depositControlService.deletePurchaseOrder(orderId);
		return ResponseEntity.ok(orderNumberDeleted);
	}
	@DeleteMapping(path="/delete-supply/{supplyId}")
	ResponseEntity<Integer> deleteSupply(@PathVariable("supplyId")long supplyId){
		int supplyNumberDeleted = depositControlService.deleteSupply(supplyId);
		return ResponseEntity.ok(supplyNumberDeleted);
	}
	
	//Find purchase order, without items.
	@GetMapping(path="/find-purchase-order/{orderId}")
	ResponseEntity<PurchaseOrderDto> findPurchaseOrderById(@PathVariable("orderId")long orderId){
		PurchaseOrderDto dto = depositControlService.findPurchaseOrder(orderId);
		return ResponseEntity.ok(dto);
	}
	@GetMapping(path="/find-supply/{supplyId}")
	ResponseEntity<SupplyDto> findSupplyById(@PathVariable("supplyId")long supplyId){
		SupplyDto dto = depositControlService.findsupply(supplyId);
		return ResponseEntity.ok(dto);
	}
}
