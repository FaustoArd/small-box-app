package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PdfTextToSupplyTest {

	@Autowired
	private PdfToStringUtils pdfToStringUtils;
	
	private String text;
	private List<String> supplyPdfList;
	private String[] arrTextSplitPageEnd;
	private String[] arrTextSplitN;

	private final String supplyNumberRegex = "^(?=.*[0-9]{1,4})";

	@BeforeAll
	void setup() throws Exception {
		text = pdfToStringUtils.pdfToReceipt("sum-551");
		arrTextSplitPageEnd = text.split("PageEnd");
		arrTextSplitN = text.split("\\n");
		// supplyPdfList.forEach(e -> System.out.println(e));
		for (String s : arrTextSplitN) {
			System.out.println(s);
		}
	}

	@Test
	void mustReturnSupply() throws Exception {
		Supply supply = new Supply();
		supply.setSupplyNumber(mustReturnSupplyNumber(arrTextSplitN));
		supply.setDate(mustReturnDate(text));
		supply.setSupplyItems(mustReturnSupplyItemList(arrTextSplitN));
		supply.setEstimatedTotalCost(getEstimatedTotal(arrTextSplitN));
		System.out.println("Estimated: "+supply.getEstimatedTotalCost());
		System.out.println("TEST: " + supply.getSupplyNumber());
		System.out.println("TEST: " + supply.getDate().getTime());
		System.out.println("TEST: " + supply.getSupplyItems());
	}
	
	private BigDecimal getEstimatedTotal(String[] arrText) {
		return new BigDecimal(Stream.of(arrText)
				.filter(f -> f.toLowerCase().contains("total"))
				.findFirst().get().replaceAll("[a-zA-Z]", "")
				.replace(":", "")
				.replace("$", "")
				.replace(".", "")
				.replace(",", ".")
				.strip());
	}

	private final String supplyNumberTitleRegex = "(SOLICITUD DE PEDIDO Nº)";

	
	private int mustReturnSupplyNumber(String[] arrText) throws Exception {
		Pattern p = Pattern.compile(supplyNumberTitleRegex);
		String number = Stream.of(arrText).filter(f -> p.matcher(f).find()).map(m -> m.substring(24, 27))
				.map(m -> m.replaceAll("[a-zA-Z\\D]", "")).collect(Collectors.joining(""));
		System.out.println("Sum number: " + number + "fin.");
		return Integer.parseInt(number);
	}

	private final String strDateV2 = "^(?=.*([0-9]{2})*([/]{1})){2}([0-9]{2,4})";

	
	private Calendar  mustReturnDate(String text) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Pattern p = Pattern.compile(strDateV2);
		String date = Stream.of(text.split(" ")).filter(f -> p.matcher(f).find()).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace("/", "-").trim();

		System.out.println("Test fecha: " + date);
		cal.setTime(sdf.parse(date));
		return cal;
	}

	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	//private final String itemMeasureUnitRegex ="(?=.*(cada)?(kilogramo)?)";

	
	private List<SupplyItem> mustReturnSupplyItemList(String[] arrText) throws Exception {
		Pattern pCode = Pattern.compile(itemCodeRegex);
		Pattern pProgCat = Pattern.compile(itemProgCatRegex);
		Pattern pQuantity = Pattern.compile(itemQuantityRegex);
		Pattern pUnitPrice = Pattern.compile(itemUnitPrice);
		//Pattern pMesaureUnit = Pattern.compile(itemMeasureUnitRegex,Pattern.CASE_INSENSITIVE);
		List<String> strItems = Stream.of(arrText).filter(f -> pCode.matcher(f).find())
				.collect(Collectors.toList());
		return  strItems.stream().map(item -> {
			SupplyItem supplyItem = new SupplyItem();
			supplyItem.setItemDetail(item.replaceAll("([0-9]*\\W)", " ").trim());
			supplyItem.setTotalEstimatedCost(new BigDecimal(item.substring(item.indexOf("$")+1)
					.replace(".", "").replace(",", ".").strip()));
			ListIterator<String> list = Stream.of(item.split(" ")).toList().listIterator();
			list.forEachRemaining(i -> {
				if (pCode.matcher(i).find()) {
					supplyItem.setCode(i);
				}
				if (pProgCat.matcher(i).matches()) {
					supplyItem.setProgramaticCat(i);
				}
				if (pQuantity.matcher(i).find()) {
					supplyItem.setQuantity(i);
				}
				if(pUnitPrice.matcher(i).find()) {
					i= i.replace(".", "");
					i = i.replace(",", ".");
							
					supplyItem.setUnitCost(new BigDecimal(i));
				}
				if(i.toLowerCase().contains("cada")) {
					i = i + " UNO";
					supplyItem.setMeasureUnit(i);
				}if( i.toLowerCase().contains("kilogramo")) {
					supplyItem.setMeasureUnit(i);
				}

			});

			return supplyItem;
		}).toList();
		
	}
	
	/*private int findPointsQuantity(String text) {
		char[] arrText = text.toCharArray();
		int count = 0;
		for(int i = 0;i<arrText.length;i++) {
			if(arrText[i]=='.') {
				count ++;
			}
		}
	}*/

	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile("^(?=.*([0-9].)*(,)([0-9]){5})", Pattern.CASE_INSENSITIVE);
		// Matcher m = p2.matcher("P.V. Nro. 00007 -");
		// System.out.println("test: "+arrTextSplitN[2]);
		assertTrue(p2.matcher("63,84000").find());
	}

}
