package com.lord.small_box.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.lord.small_box.dtos.ReceiptDto;
import com.lord.small_box.mappers.ReceiptCustomMapper;
import com.lord.small_box.services_impl.DocumentAIServiceImpl;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/document_ai")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DocumentAIController {
	
	@Autowired
	private final DocumentAIServiceImpl documentAIService;
	
	@Autowired
	private final ReceiptCustomMapper receiptCustomMapper;
	
	
	
	
	private static final Gson gson = new Gson();
	
	@PostMapping(path = "/upload_file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ResponseEntity<ReceiptDto> getDocumentParagraphs(@RequestPart("file") MultipartFile file) throws IOException, InterruptedException, ExecutionException, TimeoutException{
		Multimap<String, String> response  = documentAIService.getDocumentResponse(file.getOriginalFilename());
		ReceiptDto receiptDto = receiptCustomMapper.entityListToDto(response);
		return  new ResponseEntity<ReceiptDto>(receiptDto,HttpStatus.OK);
	}
	
	
	
}
