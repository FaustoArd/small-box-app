package com.lord.small_box.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.text_analisys.TextToDispatch;
import com.lord.small_box.text_analisys.TextToReceipt;
import com.lord.small_box.utils.PdfToStringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/pdf_to_text")
@RequiredArgsConstructor
public class PdfToStringUtilsController {

	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	@Autowired
	private final TextToReceipt textToReceipt;

	@Autowired
	private final TextToDispatch textToDispatch;

	private static final Gson gson = new Gson();

	@GetMapping("/pdf-to-string")
	ResponseEntity<String> pdftoString(@RequestParam("file")String file) throws Exception{
		String result = pdfToStringUtils.pdfToString(file);
		return ResponseEntity.ok(gson.toJson(result));
	}
	
	@GetMapping("/pdf-to-list")
	ResponseEntity<List<String>> pdfToList(@RequestParam("file") String file) throws Exception {
		List<String> result = pdfToStringUtils.pdfToDispatch(file);
		// ReceiptDto receiptDto = textToReceipt.pdfReceiptToReceipt(result);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/pdf-to-receipt")
	ResponseEntity<List<SmallBox>> pdfToReceipt(@RequestParam("file") String file) throws Exception {
		String pdfText = pdfToStringUtils.pdfToString(file);
		List<SmallBox> smList = textToReceipt.getPdfToSmallBoxList(pdfText);
		return ResponseEntity.ok(smList);
		
	}

	@GetMapping("/pdf-to-dispatch")
	ResponseEntity<List<DispatchControl>> pdfToDispatch(@RequestParam("file") String file) throws Exception {
		List<String> pdfList = pdfToStringUtils.pdfToDispatch(file);
		List<DispatchControl> dispatchControl = textToDispatch.textToDispatch(pdfList);
		return ResponseEntity.ok(dispatchControl);
	}

	@GetMapping("/test-pattern")
	ResponseEntity<Boolean> testPattern(@RequestParam("text") String text, @RequestBody String pattern) {
		return ResponseEntity.ok(pdfToStringUtils.testPattern(text, pattern));
	}

	
}
