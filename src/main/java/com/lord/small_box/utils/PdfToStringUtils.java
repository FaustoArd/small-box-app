package com.lord.small_box.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import java.util.List;


import java.util.regex.Pattern;



import org.apache.pdfbox.Loader;

import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.platform.commons.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.lord.small_box.exceptions.InvalidFileException;



@Component
public class PdfToStringUtils {
	
	
	
	
	public String pdfToString(String filename)throws Exception{
		String filePath = "D:\\filetest\\" + filename;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		try {
			PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBuffer(fis));
			PDFTextStripper pdfTextStripper = new PDFTextStripper();
			pdfTextStripper.setStartPage(1);
			//pdfTextStripper.setSpacingTolerance(20);
			//pdfTextStripper.setIndentThreshold(-20);
			//pdfTextStripper.setLineSeparator("@@");
			pdfTextStripper.setSortByPosition(true);
			pdfTextStripper.setPageEnd("PageEnd");
			//pdfTextStripper.setDropThreshold(9);
			String documentText = pdfTextStripper.getText(pdfDocument);
			pdfDocument.close();
			fis.close();
			return documentText;
		} catch (IOException ex) {
			throw new InvalidFileException("Archivo no compatible", ex.getCause());
		}
		
	}
	
	public List<String> pdfToDispatch(String fileName) throws Exception {
		String filePath = "D:\\filetest\\" + fileName  ;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		try {
			PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBuffer(fis));
			PDFTextStripper pdfTextStripper = new PDFTextStripper();
			pdfTextStripper.setStartPage(1);
			String documentText = pdfTextStripper.getText(pdfDocument);
			List<String> result = Arrays.asList(documentText.split("\n"));
			pdfDocument.close();
			fis.close();
			return result;
		} catch (IOException ex) {
			throw new InvalidFileException("Archivo no compatible", ex.getCause());
		}
		
	}
	
	/*public String pdfParserTest(String filename)throws Exception {
		String filePath = "D:\\filetest\\FACTURA-40\\" + filename + ".pdf" ;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		PDFParser parser = new PDFParser(new RandomAccessReadBuffer(fis));
		PDDocument doc = parser.parse();
		PDFTextStripper pdfTextStripper = new PDFTextStripper();
		pdfTextStripper.setStartPage(1);
		pdfTextStripper.setSortByPosition(true);
		String documentText = pdfTextStripper.getText(doc);
		doc.close();
		fis.close();
		return documentText;
	}*/

	

	

	public boolean testPattern(String text, String pattern) {
		return Pattern.matches(pattern, text);
	}

}
