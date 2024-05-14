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
import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.DepositItemComparatorDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.models.ExcelItemContainer;
import com.lord.small_box.models.Organization;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.services.ExcelItemService;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.utils.ExcelToListUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/deposit-control")
@RequiredArgsConstructor
public class DepositControlController {

	@Autowired
	private final DepositControlService depositControlService;
	
	@Autowired
	private final ExcelToListUtils excelToListUtils;
	
	@Autowired
	private final ExcelItemService excelItemService;
	
	@Autowired
	private final OrganizationService organizationService;
	
	private static final Gson gson = new Gson();
	
	@GetMapping(path="/find-deposit-controls-by-deposit")
	ResponseEntity<List<DepositControlDto>> findDepositControlsByDeposit(@RequestParam("depositId")long depositId){
	List<DepositControlDto> controlsDto = depositControlService.findDepositControlsByDeposit(depositId);
	return ResponseEntity.ok(controlsDto);
	}
	@PostMapping(path="/create-deposit")
	ResponseEntity<String> createDeposit(@RequestBody DepositDto depositDto){
		String depositName = depositControlService.createDeposit(depositDto);
		return new ResponseEntity<String>(gson.toJson(depositName),HttpStatus.CREATED);
	}
	@GetMapping(path="/find-deposits")
	ResponseEntity<List<DepositDto>> findDeposits(@RequestParam("organizationId")long organizationId){
		List<DepositDto> depositsDto = depositControlService.findAllDepositsByOrganization(organizationId);
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
	@PostMapping(path="/create-big-bag")
	ResponseEntity<BigBagDto> createBigBag(@RequestBody BigBagDto bigBagDto,@RequestParam("organizationId")long organizationId){
		BigBagDto savedBigBagDto = depositControlService.createBigBag(bigBagDto,organizationId);
		return new ResponseEntity<BigBagDto>(savedBigBagDto,HttpStatus.CREATED);
	}
	@GetMapping(path="/find-big-bags")
	ResponseEntity<List<BigBagDto>> findAllBigBagsByOrganization(@RequestParam("organizationId")long organizationId){
		List<BigBagDto> bigBagDtos = depositControlService.findAllBigBagsByOrg(organizationId);
		return ResponseEntity.ok(bigBagDtos);
	}
	
	@GetMapping(path="/find-big-bag-items")
	ResponseEntity<List<BigBagItemDto>> findAllBigBagItems(@RequestParam("bigBagId")long bigBagId){
		List<BigBagItemDto> dtos = depositControlService.findAllBigBagItems(bigBagId);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping(path="/update-big-bag-item-quantity")
	ResponseEntity<BigBagItemDto> updateBigBagItemQuantity(@RequestParam("bigBagItemId")long bigBagItemId,@RequestParam("quantity")int quantity){
		BigBagItemDto dto = depositControlService.updateBigBagItemQuantity(bigBagItemId,quantity);
		return  ResponseEntity.ok(dto);
	}
	@GetMapping(path="/calculate-big-bag-total-quantity")
	ResponseEntity<Integer> calculatebigBagTotalQuantity(@RequestParam("bigBagId")long bigBagId,@RequestParam("depositId")long depositId){
		int result = depositControlService.getTotalBigBagQuantityAvailable(bigBagId, depositId);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping(path="/delete-deposit-control/{depositControlId}")
	ResponseEntity<String> deleteDepositControlById(@PathVariable("depositControlId")long depositControlId){
		String deletedControlCode = depositControlService.deleteDepositControlById(depositControlId);
		return ResponseEntity.ok(gson.toJson(deletedControlCode));
	}
	@GetMapping(path="/find-deposit-control/{depositControlId}")
	ResponseEntity<DepositControlDto> findDepositControlById(@PathVariable("depositControlId")long depositControlId){
		DepositControlDto depositControlDto = depositControlService.findDepositControlById(depositControlId);
		return ResponseEntity.ok(depositControlDto);
	}
	@PutMapping(path="/update-deposit-control")
	ResponseEntity<DepositControlDto> updateDepositControl(@RequestBody DepositControlDto depositControlDto,
			@RequestParam("depositId")long depositId){
		DepositControlDto updatedDepositControlDto = depositControlService.updateDepositControl(depositControlDto,depositId);
		return new ResponseEntity<DepositControlDto>(updatedDepositControlDto,HttpStatus.OK);
	}
	
	
	@PostMapping(path="/excel-order-comparator",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<List<DepositItemComparatorDto>> generateExcelToOrderComparator(@RequestPart("file")MultipartFile file,
			@RequestParam("organizationId")long organizationId) throws Exception{
		List<ExcelItemDto> excelItemDtos = excelToListUtils.excelDataToDeposit(file);
		Organization org = organizationService.findById(organizationId);
		ExcelItemContainer excelItemContainer = excelItemService.saveExcelItemContainer(org);
		List<ExcelItemDto> savedExcelItemDtos = excelItemService.saveExcelItems(excelItemDtos, excelItemContainer);
		List<DepositItemComparatorDto> comparatorsDto = depositControlService.getExcelToPuchaseOrderComparator(savedExcelItemDtos, organizationId);
		return new ResponseEntity<List<DepositItemComparatorDto>>(comparatorsDto,HttpStatus.CREATED);
	}
	
	@PostMapping(path="/save-excel-items-to-deposit")
	ResponseEntity<List<DepositControlDto>> saveExcelItemsToDeposit(@RequestParam("organizationId")long organizationId
			,@RequestParam("depositId")long depositId,@RequestBody List<ExcelItemDto> excelItemDtos){
		List<DepositControlDto> depositControlDtos = depositControlService
				.saveExcelItemsToDepositControls(organizationId, depositId, excelItemDtos);
		return new ResponseEntity<List<DepositControlDto>>(depositControlDtos,HttpStatus.CREATED);
	}
	
	
}
