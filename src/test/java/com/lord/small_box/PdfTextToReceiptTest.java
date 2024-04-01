package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.stream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.lord.small_box.text_analisys.TextToReceipt;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PdfTextToReceiptTest {

	private final String strDateV2 = "^(([0-9]{2})*([-/]){1}){2}([0-9]{2,4})";
	private final String totalRegex1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[tT])(?![@#$%^&+=]).{4,4}$";
	private final String totalRegex2 = "^(?=.*[tTiI\\s]).{1}([tToOaA0-9\\s]).{3}(.?)*$";
	private final String totalRegex3 = "^([t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String totalRegex4Current = "^(?=.?[^sub]*?[t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String patternTicketTotalV3 = "^(([0-9]+)+[.,]+)+([0-9]{2})$";
	private final String patternDateTitle = "^((fecha)[:;]?)";
	private final String strTicketFirst = "^(?=.*[0-9]{4,5}-[0]{2}[0-9]{6}[.]*)";
	private final String strTicketTwoPart = "^.*(?=.*[0-9]{4,5})(.)*([0]{2}[0-9]{6}[.]*)";
	private final String strTicketTwoPartP1 = "^.*([p][.][v])+(.)*(?=.*[0-9]{4,5})";

	@Autowired
	private PdfToStringUtils pdfToStringUtils;

	

	private String text;

	private String[] arrTextSplitLine;

	@BeforeAll
	void setup() throws Exception {
		text = pdfToStringUtils.pdfToString("2FACTURA-4");
		arrTextSplitLine = text.split("\\n");
	}
	
	@Test
	void mustReturnTotalNumber() throws Exception {
		Pattern pTotal = Pattern.compile(totalRegex4Current, Pattern.CASE_INSENSITIVE);
		String result = Stream.of(arrTextSplitLine).filter(f -> pTotal.matcher(f.trim()).find())
				.map(m -> m.replace("$", "").replace("%", "")).map(this::getTotal).findFirst().get();
		System.out.println("Total Result: " + result);
	}

	private String getTotal(String line) {
		line = line.replace("„", ".").replace(",,", ".").replace(";;", ".").replace("O", "0");
		return Stream.of(line.split(" ")).filter(f -> f.trim().matches(patternTicketTotalV3)).findFirst().get()
				.replace(",", ".");
	}

	private final Pattern patternTicketNumberFull = Pattern.compile(strTicketTwoPart, Pattern.CASE_INSENSITIVE);

	//@Test
	void mustReturnTicketNumber() throws Exception {
		String result = Stream.of(arrTextSplitLine).filter(f -> patternTicketNumberFull.matcher(f).find())
				.map(this::getTicketNumber).findFirst().get();
		System.out.println("Ticket Number First Result:" + result);
	}

	private String getTicketNumber(String line) {
		return Stream.of(line.split(" ")).filter(f -> patternTicketNumberFull.matcher(f.trim()).find()).findFirst()
				.get();
	}
	
	private final Pattern patternTicketTwoPart =  Pattern.compile(strTicketTwoPart);
	
	@Test
	void mustReturnTwoPartTicketNumber()throws Exception{
		String result = Stream.of(arrTextSplitLine).filter(f -> patternTicketTwoPart.matcher(f).find()).findFirst().get();
		System.out.println("two part ticket number: "+result);
	} 
	
	private final Pattern patternTicketPartOne =  Pattern.compile(strTicketTwoPartP1);

	//@Test
	void mustReturnTicketPartOne()throws Exception{
		Stream.of(arrTextSplitLine).forEach(e -> System.out.println(e));
		String result =  Stream.of(arrTextSplitLine).filter(f -> patternTicketPartOne.matcher(f.trim()).find()).findFirst().get();
		System.out.println("ticket part one result: " + result);
	}
	
	@Test
	void mustReturnProviderName()throws Exception{
		
		String result =  Stream.of(arrTextSplitLine).findFirst().get().trim();
		System.out.println("Provider: " + result);
	}
	
	
	private final Pattern patternDate = Pattern.compile(strDateV2,Pattern.CASE_INSENSITIVE);
	
	@Test
	void mustReturnDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Stream.of(arrTextSplitLine).forEach(e -> System.out.println(e));
		String[] result=  Stream.of(text.split(" "))
				.filter(f -> patternDate.matcher(f).find())
				.sorted((s2,s1)-> s1.compareTo(s2))
				.collect(Collectors.joining(" ")).split(" ");
		try {
		if(result.length==1) {
			cal.setTime(sdf.parse(result[0].replace(result[0].substring(6, 8), "20" +result[0].substring(6, 8)).replace("/", "-")));
			System.out.println( "Date Result index0:" + cal.getTime());
		}else {
			cal.setTime(sdf.parse(result[1].replace(result[1].substring(6, 8), "20" +result[1].substring(6, 8)).replace("/", "-")));
			System.out.println("Date result index1: " +cal.getTime());
		}
		}catch (Exception e) {
			throw new RuntimeException("Error al parsear la fecha");
		}
	}
	
	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile("", Pattern.CASE_INSENSITIVE);
		//Matcher m = p2.matcher("P.V. Nro. 00007 -");
		assertTrue(p2.matcher("P.V. Nro. 00007 -").find());
	}
}
