package com.lord.small_box.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.lord.small_box.dtos.DispatchControlDto;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.ReceiptDto;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.text_analisys.TextToDispatch;
import com.lord.small_box.text_analisys.TextToReceipt;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/pdf_to_text")
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
	ResponseEntity<String> pdftoString(@RequestParam("file")String file) throws Exception{
		String result = pdfToStringUtils.pdfToReceipt(file);
		return ResponseEntity.ok(gson.toJson(result));
	}
	
	@GetMapping("/pdf_to_list")
	ResponseEntity<List<String>> pdfToList(@RequestParam("file") String file) throws Exception {
		List<String> result = pdfToStringUtils.pdfToDispatch(file);
		// ReceiptDto receiptDto = textToReceipt.pdfReceiptToReceipt(result);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/pdf_to_receipt")
	ResponseEntity<List<SmallBox>> pdfToReceipt(@RequestParam("file") String file) throws Exception {
		String pdfText = pdfToStringUtils.pdfToReceipt(file);
		List<SmallBox> smList = textToReceipt.getPdfToSmallBoxList(pdfText);
		return ResponseEntity.ok(smList);
		
	}

	@GetMapping("/pdf_to_dispatch")
	ResponseEntity<List<DispatchControl>> pdfToDispatch(@RequestParam("file") String file) throws Exception {
		List<String> pdfList = pdfToStringUtils.pdfToDispatch(file);
		List<DispatchControl> dispatchControl = textToDispatch.textToDispatch(pdfList);
		return ResponseEntity.ok(dispatchControl);
	}

	@GetMapping("/test_pattern")
	ResponseEntity<Boolean> testPattern(@RequestParam("text") String text, @RequestBody String pattern) {
		return ResponseEntity.ok(pdfToStringUtils.testPattern(text, pattern));
	}

	
}
