package com.lord.small_box.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.documentai.v1.Document;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.lord.small_box.dtos.ReceiptDto;
import com.lord.small_box.mappers.ReceiptCustomMapper;
import com.lord.small_box.services.DocumentAIService;
import com.lord.small_box.services_impl.DocumentAIServiceImpl;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/document_ai")
@RequiredArgsConstructor
public class DocumentAIController {
	
	@Autowired
	private final DocumentAIServiceImpl documentAIService;
	
	@Autowired
	private final ReceiptCustomMapper receiptCustomMapper;
	
	
	
	
	private static final Gson gson = new Gson();
	
	@GetMapping("/get_paragraphs")
	ResponseEntity<ReceiptDto> getDocumentParagraphs() throws IOException, InterruptedException, ExecutionException, TimeoutException{
		Multimap<String, String> response  = documentAIService.quickStart();
		ReceiptDto receiptDto = receiptCustomMapper.entityListToDto(response);
		return  ResponseEntity.ok(receiptDto);
	}
	
	@GetMapping("/test")
	String test() {
		ReceiptDto receiptDto = new ReceiptDto();
		return "";
	}

}
