package com.lord.small_box.text_analisys;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lord.small_box.models.ReceiptDto;
import com.lord.small_box.utils.PdfToStringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TextToReceipt {

	private final String patternDateDash = "^(([0-9]{2}){2}[,.]{1})([0-9]{4})$";
	private final String patternDateV2 = "^(([0-9]{2})*([-/]){1}){2}([0-9]{4})";
	private final String patternTicketTotal = "^(([0-9]+)+[.,])+([0-9]{2})$";
	private final String patternTicketTotalV2 = "^(([0-9a-zA-Z])*[.,]*([a-zA-Z]{0,2}))+([0-9]{2})$";
	private final String patternTicketTotalV3 = "^(([0-9]+)+[.,]+)+([0-9]{2})$";
	private final String patterntTotalTitle = "(^(?=.*[total]))";
	
	
	public List<String> getPdfList(String pdfText) {
		Pattern pattern = Pattern.compile(patternTicketTotalV3,Pattern.CASE_INSENSITIVE);
		String pattern2 = "^(?=.*[tTiI\\s]).{1}([tToOaA0-9\\s]).{3}(.?)*$";
		String pattern3 = patterntTotalTitle;
		Pattern p = Pattern.compile(pattern3);
		
		Matcher m;// = p.matcher("Total:");
		List<String> pdfList = Arrays.asList(pdfText.split("@@"));
		pdfList.forEach(e -> System.out.println(e));
		System.out.println("Doc count:" + Stream.of(pdfText.split("@@")).count());
		return pdfList;
	
		}
	
	private String splitTotal(String line) {
		String[] splitted = line.split(" ");
		//String result = Stream.of(splitted).filter(f -> f.matches(patternTicketTotalV3)).findFirst().get();
		String test = "test: " ;
		for(String s:splitted) {
			System.out.println("split for: " + s);
			if(s.toLowerCase().strip().matches("^(?=.*[tTiI\\s]).{1}([tToOaA0-9\\s]).{3}(.?)*$")) {
				System.out.println("Pattern total ");
				continue;
			}
			if(s.matches(patternTicketTotalV3)) {
				System.out.println("Matches pticketv3: " +s);
				
				return s.replace(",", ".");
			}else {
			test  =s.replace(",", ".");
			}
		}
		
		return  test;
	}
	
	public ReceiptDto pdfReceiptToReceipt(List<String> pdfText) {
		ReceiptDto receiptData = mapPdfToReceipt(pdfText);
		return receiptData;
		}
	
	private ReceiptDto mapPdfToReceipt(List<String> pdfReceipt) {
		ReceiptDto receiptDto = new ReceiptDto();
		String strReceipt = pdfReceipt.stream().map(r -> r).collect(Collectors.joining(" "));
		//receiptDto.setReceipt_date(getReceiptDate(strReceipt));
		//receiptDto.setTotal_price(getReceiptTotal(strReceipt));
		return receiptDto;
	}
	private String getReceiptDate(String text) {
		
		String[] arrayFecha = text.split(" ");
		ArrayList<String> arrayResult = new ArrayList<>();
		for (String str : arrayFecha) {
			str = str.replace("-", "").strip();
			if (str.matches(patternDateV2)) {
				arrayResult.add(str);
				System.out.println("Date: " + str);
				System.out.println("Date:  Length:" + str.length());
			}
			arrayResult.forEach(e -> System.out.println("array: " + e));
		}
		return arrayResult.stream().sorted((a2, a1) -> a1.compareTo(a2)).findFirst().get();
		/*
		 * return arrayResult.stream().map(r -> r.replace("," ,"")).map(r ->
		 * r.replace(";" ,"")) .sorted(Comparator.reverseOrder()).sorted((a2,a1)
		 * ->a1.compareTo(a2)).findFirst().get();
		 */

	}

	// private final String patternDateDash =
	// "^(([0-9]{2})*(-)*{1}(/)*{1}){2}([0-9]{4})";

	/*private String getReceiptTotal(String text) {
		String[] arrayTotal = text.split(" ");
		ArrayList<String> arrayResult = new ArrayList<>();
		for (String s : arrayTotal) {
			s = s.strip();
			if (s.matches(patternTicketTotal)) {
				arrayResult.add(s);
				System.out.println("Total: " + s);
				System.out.println("Total lenght: " + s.length());
			}

		}
		return arrayResult.stream().filter(f -> f.contains(".")).map(m -> m.replace(".", "").replace(",", "."))
				.map(m -> Double.parseDouble(m)).max((c2, c1) -> c2.compareTo(c1)).get().toString();

	}*/

}
