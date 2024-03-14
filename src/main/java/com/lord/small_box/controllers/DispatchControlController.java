package com.lord.small_box.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.lord.small_box.dtos.DispatchControlDto;
import com.lord.small_box.mappers.DispatchControlMapper;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.services.DispatchControlService;
import com.lord.small_box.text_analisys.TextToDispatch;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/dispatchs")
@RequiredArgsConstructor
public class DispatchControlController {
	
	@Autowired
	private final DispatchControlService dispatchControlService;
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	@Autowired
	private final TextToDispatch textToDispatch;
	
	private static final Gson gson = new Gson();
	
	private static final Logger log = LoggerFactory.getLogger(DispatchControlController.class);
	
	@PostMapping("/create_dispatch")
	 ResponseEntity<String> createDispatch(@RequestBody DispatchControlDto dispatchControlDto){
		 DispatchControl dispatchControl = DispatchControlMapper.INSTANCE.dtoToDispatch(dispatchControlDto);
		 String result  = dispatchControlService.createDispatch(dispatchControl);
		 return new ResponseEntity<String>(gson.toJson(result),HttpStatus.OK);
	 }
	
	@DeleteMapping("/delete_dispatch/{id}")
	ResponseEntity<String> deleteDispatch(@PathVariable("id") Long id){
		String result = dispatchControlService.deleteById(id);
		return ResponseEntity.ok(gson.toJson(result));
	}
	
	@GetMapping("/find_dispatch/{id}")
	ResponseEntity<DispatchControlDto> findDistpachById(@PathVariable("id")Long id){
		DispatchControl dispatchControl = dispatchControlService.findById(id);
		DispatchControlDto dispatchControlDto = DispatchControlMapper.INSTANCE.dispatchToDto(dispatchControl);
		return ResponseEntity.ok(dispatchControlDto);
	}
	@GetMapping("/find_all_dispatch_by_org_paging")
	ResponseEntity<List<DispatchControlDto>> findAllDispatchsByOrganizationIdPagingAndSorting(@RequestParam("organizationId")Long organizationId,
			@RequestParam(defaultValue = "0")Integer pageNo,
			@RequestParam(defaultValue = "10")Integer pageSize,
			@RequestParam(defaultValue = "date")String sortBy){
		List<DispatchControl> dispatchControls = dispatchControlService
				.findAllDispatchControlByOrganizationPagingAndSorting(organizationId,pageNo,pageSize,sortBy);
		List<DispatchControlDto> dispatchControlDtos = DispatchControlMapper.INSTANCE.dispatchsToDtos(dispatchControls);
		return ResponseEntity.ok(dispatchControlDtos);
	}
	@GetMapping("/find_all_dispatch_by_org_example_paging")
	ResponseEntity<List<DispatchControlDto>> findAllDispatchsByOrganizationIdByExamplePagingAndSorting(
			@RequestParam("organizationId")Long organizationId,
			@RequestParam("example")String example,
			@RequestParam(defaultValue = "0")Integer pageNo,
			@RequestParam(defaultValue = "20")Integer pageSize,
			@RequestParam(defaultValue = "date")String sortBy){
		log.info("Find Dispatch By Example. Paging", organizationId, example, pageNo, pageSize, sortBy);
		
		System.out.println("Controller example: " + example);
		List<DispatchControl> dispatchControls = dispatchControlService
				.findAllDispatchControlByOrgByExamplePagingAndSorting(organizationId,example,pageNo,pageSize,sortBy);
		List<DispatchControlDto> dispatchControlDtos = DispatchControlMapper.INSTANCE.dispatchsToDtos(dispatchControls);
		return ResponseEntity.ok(dispatchControlDtos);
	}
	
	@GetMapping("/find_all_dispatch_by_org")
	ResponseEntity<List<DispatchControlDto>> findAllDistpachControlsByOrganization(@RequestParam("organizationId")Long organizationId){
		List<DispatchControl> dispatchControls = dispatchControlService
				.findAllDistpachControlsByOrganization(organizationId);
		List<DispatchControlDto> dispatchControlDtos = DispatchControlMapper.INSTANCE.dispatchsToDtos(dispatchControls);
		return ResponseEntity.ok(dispatchControlDtos);
	}
	
	@PostMapping("/dispatch_work_template")
	ResponseEntity<String> dispatchWorkTemplate(@RequestParam("workTemplateId")Long workTemplateId){
		String dispatchResult = dispatchControlService.dispatchWorkTemplate(workTemplateId);
		return new ResponseEntity<String>(gson.toJson(dispatchResult),HttpStatus.OK);
	}
	
	@PostMapping(path = "/upload_file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<List<DispatchControlDto>> getPdfToString(@RequestPart("file") MultipartFile file,@RequestParam("organizationId")Long organizationId) throws Exception {
		List<String> pdfList = pdfToStringUtils.pdfToDispatch(file.getOriginalFilename());
		List<DispatchControl> dispatchControl = textToDispatch.textToDispatch(pdfList);
		List<DispatchControl> savedDispatchs = dispatchControlService.saveAllDispatchs(dispatchControl, organizationId);
		List<DispatchControlDto> dtos = DispatchControlMapper.INSTANCE.dispatchsToDtos(savedDispatchs);
		return new ResponseEntity<List<DispatchControlDto>>(dtos, HttpStatus.OK);

	}
	
	/*@GetMapping("/pattern_test")
	ResponseEntity<String> testMatcher(@RequestParam("match")String match){
		
		String result = dispatchControlService.exampleMatchToDispatchObject(match);
		return ResponseEntity.ok(gson.toJson(result));
	}*/
	
	

}
