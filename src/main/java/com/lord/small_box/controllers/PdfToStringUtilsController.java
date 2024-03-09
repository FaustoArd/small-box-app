package com.lord.small_box.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/pdf_to_string")
@RequiredArgsConstructor
public class PdfToStringUtilsController {
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	private static final Gson gson = new Gson();
	
	@GetMapping("/")
	ResponseEntity<String> getPdfString() throws IOException{
		String result = pdfToStringUtils.pdfToString();
		return ResponseEntity.ok(gson.toJson(result));
	}

}
