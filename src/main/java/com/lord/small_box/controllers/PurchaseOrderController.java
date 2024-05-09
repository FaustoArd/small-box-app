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
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.services.PurchaseOrderService;
import com.lord.small_box.utils.PdfToStringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/purchase-order")
@RequiredArgsConstructor
public class PurchaseOrderController {
	
	@Autowired
	private final PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	//ORDER
		@GetMapping(path = "/find-all-orders-by-org")
		ResponseEntity<List<PurchaseOrderDto>> findAllOrdersByOrganization(@RequestParam("organizationId")long organizationId){
			List<PurchaseOrderDto> purchaseOrderDtos =purchaseOrderService.findAllOrdersByOrganizationId(organizationId);
			return ResponseEntity.ok(purchaseOrderDtos);
		}
		@GetMapping(path="/find-order-items")
		ResponseEntity<List<PurchaseOrderItemDto>> findPurchaseOrderItems(@RequestParam("purchaseOrderId")long purchaseOrderId){
			
			List<PurchaseOrderItemDto> itemDtos = purchaseOrderService.findPurchaseOrderItems(purchaseOrderId);
			return ResponseEntity.ok(itemDtos);
		}
		@PutMapping(path = "/load-order-to-deposit")
		ResponseEntity<List<PurchaseOrderToDepositReportDto>> loadPurchaseOrdertoDeposit
		(@RequestBody long purchaseOrderId,@RequestParam("depositId")long depositId){
			List<PurchaseOrderToDepositReportDto> loadReport = purchaseOrderService.loadPurchaseOrderToDepositControl(purchaseOrderId,depositId);
			return new ResponseEntity<List<PurchaseOrderToDepositReportDto>>(loadReport,HttpStatus.OK);
		}
		@PostMapping(path="/collect-purchase-order-pdf",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
		ResponseEntity<PurchaseOrderDto> collectPurchaseOrderFromText(@RequestPart("file")MultipartFile file,
				@RequestParam("organizationId")long OrganizationId) throws Exception{
			String text = pdfToStringUtils.pdfToString(file.getBytes());
			PurchaseOrderDto purchaseOrderDto = purchaseOrderService.collectPurchaseOrderFromText(text, OrganizationId);
			return new ResponseEntity<PurchaseOrderDto>(purchaseOrderDto,HttpStatus.CREATED);
		}
		//Find purchase order, with items.
				@GetMapping(path ="/find-full-purchase-order/{purchaseOrderId}")
				ResponseEntity<PurchaseOrderDto> findFullPurchaseOrder(@PathVariable("purchaseOrderId")long purchaseOrderId){
					PurchaseOrderDto purchaseOrderDto = purchaseOrderService.findFullPurchaseOrder(purchaseOrderId);
					return ResponseEntity.ok(purchaseOrderDto);
				}
				
				//Find purchase order, without items.
				@GetMapping(path="/find-purchase-order/{orderId}")
				ResponseEntity<PurchaseOrderDto> findPurchaseOrderById(@PathVariable("orderId")long orderId){
					PurchaseOrderDto dto = purchaseOrderService.findPurchaseOrder(orderId);
					return ResponseEntity.ok(dto);
				}
				@DeleteMapping(path="/delete-purchase-order/{orderId}")
				ResponseEntity<Integer> deletePurchaseOrder(@PathVariable("orderId")long orderId){
					int orderNumberDeleted = purchaseOrderService.deletePurchaseOrder(orderId);
					return ResponseEntity.ok(orderNumberDeleted);
				}

}
