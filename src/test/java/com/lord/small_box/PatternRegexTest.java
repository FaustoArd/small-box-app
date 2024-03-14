package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class PatternRegexTest {

	@Autowired
	private PdfToStringUtils pdfToStringUtils;

	@Autowired
	private TextToReceipt textToReceipt;

	@Test
	void stringWithReceiptDataMustReturnTotalNumber()throws Exception{
		Pattern p2 = Pattern.compile(totalRegex4Current,Pattern.CASE_INSENSITIVE);
		//Matcher m = p2.matcher("ti otal");
		
		
		String text = pdfToStringUtils.pdfToString("FACTURA-47 - Copy");
		String[] arrTextSplitLine = text.split("\\n");
		Stream.of(arrTextSplitLine).forEach(e -> System.out.println(e));
		String result = Stream.of(arrTextSplitLine)
				.filter(f -> p2.matcher(f.trim()).find()).map(m -> m.replace("$", "").replace("%", ""))
				.map(this::getTotal).findFirst().get();
		System.out.println("Result: " +result);
	}
	
	private String getTotal(String line) {
		System.out.println("get total line " + line);
		return Stream.of(line.split(" ")).filter(f -> f.trim().matches(patternTicketTotalV3)).findFirst().get().replace(",", ".");
	}

	private final String totalRegex1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[tT])(?![@#$%^&+=]).{4,4}$";
	private final String totalRegex2 = "^(?=.*[tTiI\\s]).{1}([tToOaA0-9\\s]).{3}(.?)*$";
	private final String totalRegex3 = "^([t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String totalRegex4Current = "^(?=.?[^sub]*?[t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String patternTicketTotalV3 = "^(([0-9]+)+[.,]+)+([0-9]{2})$";
	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile(totalRegex4Current, Pattern.CASE_INSENSITIVE);
		Matcher m = p2.matcher("Total:");
		assertTrue(m.matches());
	}
}
