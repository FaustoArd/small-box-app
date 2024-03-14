package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

	private final String patternDateV2 = "^(([0-9]{2})*([-/]){1}){2}([0-9]{2,4})";
	private final String totalRegex1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[tT])(?![@#$%^&+=]).{4,4}$";
	private final String totalRegex2 = "^(?=.*[tTiI\\s]).{1}([tToOaA0-9\\s]).{3}(.?)*$";
	private final String totalRegex3 = "^([t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String totalRegex4Current = "^(?=.?[^sub]*?[t]{1}[o]{1}[t]{1}[a]{1}[l]{1}[:;]?\\s*)";
	private final String patternTicketTotalV3 = "^(([0-9]+)+[.,]+)+([0-9]{2})$";
	private final String patternDateTitle = "^((fecha)[:;]?)";
	private final String patternTicketFirst = "^(?=.*[0-9]{4,5}-[0]{2}[0-9]{6}[.]*)";

	@Autowired
	private PdfToStringUtils pdfToStringUtils;

	@Autowired
	private TextToReceipt textToReceipt;

	private String text;

	private String[] arrTextSplitLine;

	@BeforeAll
	void setup() throws Exception {
		text = pdfToStringUtils.pdfToString("2FACTURA-1C");
		arrTextSplitLine = text.split("\\n");
	}

	@Test
	void stringWithReceiptDataMustReturnTotalNumber() throws Exception {
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

	private final Pattern patternTicketNumberFull = Pattern.compile(patternTicketFirst, Pattern.CASE_INSENSITIVE);

	@Test
	void stringWithReceiptDataMustReturnTicketNumber() throws Exception {
		Stream.of(arrTextSplitLine).forEach(e -> System.out.println(e));
		String[] arrTextSplitLine = text.split("\\n");
		String result = Stream.of(arrTextSplitLine).filter(f -> patternTicketNumberFull.matcher(f).find())
				.map(this::getTicketNumber).findFirst().get();
		System.out.println("Ticket Number First Result:" + result);
	}

	private String getTicketNumber(String line) {
		return Stream.of(line.split(" ")).filter(f -> patternTicketNumberFull.matcher(f.trim()).find()).findFirst()
				.get();
	}

	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile(patternTicketTotalV3, Pattern.CASE_INSENSITIVE);
		Matcher m = p2.matcher("450.00");
		assertTrue(m.matches());
	}
}
