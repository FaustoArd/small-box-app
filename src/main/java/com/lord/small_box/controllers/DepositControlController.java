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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-control")
@RequiredArgsConstructor
public class DepositControlController {

	@Autowired
	private final DepositControlService depositControlService;
	
	@Autowired
	private final PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	@GetMapping(path="/find-deposit-controls-by-org")
	ResponseEntity<List<DepositControlDto>> findDepositControlsByOrganization(@RequestParam("organizationId")long organizationId){
	List<DepositControlDto> controlsDto = depositControlService.findDepositControlsByOrganization(organizationId);
	System.out.println("DEPOSIT CONTROLLER");
	controlsDto.forEach(e -> System.out.println("deposit item:"+e.getItemCode()));
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
	
	@GetMapping(path ="/find-purchase-order/{purchaseOrderId}")
	ResponseEntity<PurchaseOrderDto> findFullPurchaseOrder(@PathVariable("purchaseOrderId")long purchaseOrderId){
		PurchaseOrderDto purchaseOrderDto = depositControlService.findFullPurchaseOrder(purchaseOrderId);
		return ResponseEntity.ok(purchaseOrderDto);
	}
	
	@PutMapping(path = "/load-order-to-deposit")
	ResponseEntity<List<PurchaseOrderToDepositReportDto>> loadPurchaseOrdertoDeposit(@RequestBody long purchaseOrderId){
		List<PurchaseOrderToDepositReportDto> loadReport = depositControlService.loadPurchaseOrderToDepositControl(purchaseOrderId);
		return new ResponseEntity<List<PurchaseOrderToDepositReportDto>>(loadReport,HttpStatus.OK);
	}
	
	@GetMapping(path = "/create-supply-report")
	ResponseEntity<List<SupplyReportDto>> createSupplyResport(@RequestParam("supplyId")long supplyId){
		List<SupplyReportDto> report = depositControlService.createSupplyReport(supplyId);
		return ResponseEntity.ok(report);
	}
	
	@GetMapping(path = "/create-supply-correction-note")
	ResponseEntity<SupplyCorrectionNoteDto> createSupplyCorrectionNote(@RequestParam("supplyId")long supplyId){
		SupplyCorrectionNoteDto supplyCorrectionNote = depositControlService.createSupplyCorrectionNote(supplyId);
		return ResponseEntity.ok(supplyCorrectionNote);
	}

}
