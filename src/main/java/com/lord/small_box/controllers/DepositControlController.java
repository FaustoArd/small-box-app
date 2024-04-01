package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyCorrectionNote;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit_control")
@RequiredArgsConstructor
public class DepositControlController {

	@Autowired
	private final DepositControlService depositControlService;

	@Autowired
	private final PdfToStringUtils pdfToStringUtils;

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
	
	@GetMapping("/find-purchase-order/{purchaseOrderId}")
	ResponseEntity<PurchaseOrderDto> findFullPurchaseOrder(@PathVariable("purchaseOrderId")long purchaseOrderId){
		PurchaseOrderDto purchaseOrderDto = depositControlService.findFullPurchaseOrder(purchaseOrderId);
		return ResponseEntity.ok(purchaseOrderDto);
	}
	
	@PutMapping("/load-order-to-deposit")
	ResponseEntity<List<String>> loadPurchaseOrdertoDeposit(@RequestParam("purchaseOrderId")long purchaseOrderId){
		List<String> loadReport = depositControlService.loadPurchaseOrderToDepositControl(purchaseOrderId);
		return new ResponseEntity<List<String>>(loadReport,HttpStatus.OK);
	}
	
	@GetMapping("/create-supply-report")
	ResponseEntity<List<SupplyReportDto>> createSupplyResport(@RequestParam("supplyId")long supplyId){
		List<SupplyReportDto> report = depositControlService.createSupplyReport(supplyId);
		return ResponseEntity.ok(report);
	}
	
	@GetMapping("create-supply-correction-note")
	ResponseEntity<SupplyCorrectionNote> createSupplyCorrectionNote(@RequestParam("supplyId")long supplyId){
		SupplyCorrectionNote supplyCorrectionNote = depositControlService.createSupplyCorrectionNote(supplyId);
		return ResponseEntity.ok(supplyCorrectionNote);
	}

}
