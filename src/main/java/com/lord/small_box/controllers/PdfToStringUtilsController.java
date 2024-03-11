package com.lord.small_box.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.DispatchControlDto;
import com.lord.small_box.models.ReceiptDto;
import com.lord.small_box.text_analisys.TextToDispatch;
import com.lord.small_box.text_analisys.TextToReceipt;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/pdf_to_string")
@RequiredArgsConstructor
public class PdfToStringUtilsController {
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	@Autowired
	private final TextToReceipt textToReceipt;
	
	@Autowired
	private final TextToDispatch textToDispatch;
	
	private static final Gson gson = new Gson();
	
	
	
	@GetMapping("/pdf_to_string")
	ResponseEntity<List<String>> pdfToString(@RequestParam("file")String file) throws Exception{
		List<String> result = pdfToStringUtils.pdfToList(file);
		//ReceiptDto receiptDto = textToReceipt.pdfReceiptToReceipt(result);
		
		return ResponseEntity.ok(result);
	}
	@GetMapping("/pdf_to_receipt")
	ResponseEntity<ReceiptDto> pdfToReceipt(@RequestParam("file")String file) throws Exception{
		List<String> pdfList = pdfToStringUtils.pdfToList(file);
		ReceiptDto receiptDto = textToReceipt.pdfReceiptToReceipt(pdfList);
		return ResponseEntity.ok(receiptDto);
	}
	@GetMapping("/pdf_to_dispatch")
	ResponseEntity<List<DispatchControlDto>> pdfToDispatch(@RequestParam("file")String file) throws Exception{
		List<String> pdfList = pdfToStringUtils.pdfToList(file);
		List<DispatchControlDto> dispatchControlDto = textToDispatch.textToDispatch(pdfList);
		return ResponseEntity.ok(dispatchControlDto);
	}
	
	@GetMapping("/test_pattern")
	ResponseEntity<Boolean> testPattern(@RequestParam("text")String text, @RequestBody String pattern){
		return ResponseEntity.ok(pdfToStringUtils.testPattern(text, pattern));
	}
	
	

}
