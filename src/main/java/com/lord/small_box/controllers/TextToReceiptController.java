package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.SmallBoxDto;
import com.lord.small_box.mappers.SmallBoxMapper;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.text_analisys.TextToReceipt;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/pdf_to_samllbox")
@RequiredArgsConstructor
public class TextToReceiptController {
	
	
	private final TextToReceipt textToReceipt;
	
	@Autowired
	private final PdfToStringUtils pdfToStringUtils;
	
	ResponseEntity<List<SmallBoxDto>> getpdfToSmallBoxList(@RequestParam String filename) throws Exception{
		String text = pdfToStringUtils.pdfToReceipt(filename);
		List<SmallBox> smallBoxs = textToReceipt.getPdfToSmallBoxList(text);
		List<SmallBoxDto> smallBoxDtos = SmallBoxMapper.INSTANCE.toSmallBoxesDtos(smallBoxs);
		return  ResponseEntity.ok(smallBoxDtos);
				
	}
	
	
	
	

}
