package com.lord.small_box.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import com.lord.small_box.models.ReceiptDto;

@Component
public class PdfToStringUtils {

	public List<String> pdfToList(String fileName) throws Exception {
		String filePath = "d:\\filetest\\DOCUMENTAI-REMITOS SISTEMA\\" + fileName ;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBuffer(fis));
		PDFTextStripper pdfTextStripper = new PDFTextStripper();
		pdfTextStripper.setDropThreshold(7);
		pdfTextStripper.setStartPage(1);
		//pdfTextStripper.setEndPage(0);
		String documentText = pdfTextStripper.getText(pdfDocument);
		// System.out.println(documentText);
		List<String> result = Arrays.asList(documentText.split("\n"));
		//result.forEach(e -> System.out.println(e));
		pdfDocument.close();
		fis.close();
		return result;
	}

	

	public boolean testPattern(String text, String pattern) {
		return Pattern.matches(pattern, text);
	}

}
