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
import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyItemRequestDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.services.SupplyService;
import com.lord.small_box.utils.PdfToStringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/supply")
@RequiredArgsConstructor
public class SupplyController {
	
	@Autowired
	private final SupplyService supplyService;
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	private static final Gson gson = new Gson();
	
	@GetMapping(path ="/find-all-supplies-by-org")
	ResponseEntity<List<SupplyDto>> findAllSuppliesByOrganization(@RequestParam("organizationId")long organizationId){
		List<SupplyDto> supplyDtos = supplyService.findAllSuppliesByMainOrganizationId(organizationId);
		return ResponseEntity.ok(supplyDtos);
	}
	@GetMapping(path = "/find-supply-items")
	ResponseEntity<List<SupplyItemDto>> findSupplyItems(@RequestParam("supplyId")long supplyId){
		List<SupplyItemDto> itemDtos = supplyService.findSupplyItems(supplyId);
		return ResponseEntity.ok(itemDtos);
	}

	@PostMapping(path = "/collect-supply-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<SupplyDto> collectSupplyFromText(@RequestPart("file") MultipartFile file,
			@RequestParam("organizationId") long organizationId) throws Exception {
		String text = pdfToStringUtils.pdfToString(file.getBytes());
		SupplyDto supplyDto = supplyService.collectSupplyFromText(text,organizationId);
		return new ResponseEntity<SupplyDto>(supplyDto, HttpStatus.CREATED);
	}
	
	/*@GetMapping(path = "/create-supply-report")
	ResponseEntity<List<SupplyReportDto>> createSupplyResport
	(@RequestParam("supplyId")long supplyId,@RequestParam("depositId")long depositId){
		List<SupplyReportDto> report = supplyService.createSupplyReport(supplyId,depositId);
		return ResponseEntity.ok(report);
	}*/
	
	@GetMapping(path = "/create-supply-correction-note")
	ResponseEntity<SupplyCorrectionNoteDto> createSupplyCorrectionNote
	(@RequestParam("supplyId")long supplyId,@RequestParam("depositId")long depositId){
		SupplyCorrectionNoteDto supplyCorrectionNote = supplyService.createSupplyCorrectionNote(supplyId,depositId);
		return ResponseEntity.ok(supplyCorrectionNote);
	}
	
	@DeleteMapping(path="/delete-supply/{supplyId}")
	ResponseEntity<Integer> deleteSupply(@PathVariable("supplyId")long supplyId){
		int supplyNumberDeleted = supplyService.deleteSupply(supplyId);
		return ResponseEntity.ok(supplyNumberDeleted);
	}
	
	@GetMapping(path="/find-supply/{supplyId}")
	ResponseEntity<SupplyDto> findSupplyById(@PathVariable("supplyId")long supplyId){
		SupplyDto dto = supplyService.findsupply(supplyId);
		return ResponseEntity.ok(dto);
	}
	
	@PutMapping(path="/set-supply-organization-applicant")
	ResponseEntity<String> setSupplyOrganizationApplicant(@RequestBody OrganizationDto organizationDto,@RequestParam("supplyId")long supplyId){
		String orgName = supplyService.setOrganizationApplicant(supplyId,organizationDto.getId());
		return new ResponseEntity<String>(gson.toJson(orgName),HttpStatus.OK);
	}
	
	@GetMapping(path="/find-all-supply-items-by-organization-applicant")
	ResponseEntity<List<SupplyItemRequestDto>> findAllSupplyItemsByOrganizationApplicant
	(@RequestParam("mainOrganization")long mainOrganization, @RequestParam("organizationApplicantId")long organizationApplicantId){
		List<SupplyItemRequestDto> supplyItems = supplyService.findAllSupplyItemsByOrganizationApplicant(mainOrganization,organizationApplicantId);
		return ResponseEntity.ok(supplyItems);
	}
	@GetMapping(path="/check-supply-assigned")
	ResponseEntity<Boolean> checkOrganizationApplicantSupplyAssigned
	(@RequestParam("mainOrganizationId")long mainOrganizationId, @RequestParam("applicantOrganizationId")long applicantOrganizationId){
		boolean result = supplyService.checkOrganizationApplicantSupplyAssigned(mainOrganizationId, applicantOrganizationId);
		System.out.println("check assgned suply: " +result);
		return ResponseEntity.ok(result);
	}
}
