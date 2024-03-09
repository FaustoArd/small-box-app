package com.lord.small_box.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Component;

@Component
public class PdfToStringUtils {
	
	
	
	public String pdfToString() throws IOException {
		String filePath = "d:\\filetest\\oeste-receipt-test1.pdf";
		File file = new File(filePath);
		String parsedText;
		
		PDFParser pdfParser =  new PDFParser(new RandomAccessReadBufferedFile(file));
		PDDocument doc = new PDD
		
	
		
		
		PDFTextStripper pdfTextStripper = new PDFTextStripper();
		int pages = doc.getNumberOfPages();
		String  pageText = pdfTextStripper.getText(doc);
		
		return pageText;
		
	}

}
