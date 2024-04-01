package com.lord.small_box.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lord.small_box.dtos.SupplyDto;
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
	
	@PostMapping(path ="/load-supply-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<SupplyDto> loadSupplyFromText(@RequestPart("file")MultipartFile file)throws Exception{
		String text= pdfToStringUtils.pdfToString(file.getOriginalFilename());
		SupplyDto supplyDto = depositControlService.loadSupplyFromText(text);
		return new ResponseEntity<SupplyDto>(supplyDto,HttpStatus.OK);
	}

}
