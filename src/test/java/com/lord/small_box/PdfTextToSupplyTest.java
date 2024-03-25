package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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

	}

	private final String supplyNumberTitleRegex = "(SOLICITUD DE PEDIDO Nº)";

	@Test
	void mustReturnSupplyNumber() throws Exception {
		Pattern p = Pattern.compile(supplyNumberTitleRegex);
		String number = Stream.of(arrTextSplitN).filter(f -> p.matcher(f).find()).map(m -> m.substring(24, 27))
				.map(m -> m.replaceAll("[a-zA-Z\\D]", "")).collect(Collectors.joining(""));
		System.out.println("Sum number: " + number + "fin.");
	}

	private final String strDateV2 = "^(?=.*([0-9]{2})*([/]{1})){2}([0-9]{2,4})";

	@Test
	void mustReturnDate() throws Exception {
		Pattern p = Pattern.compile(strDateV2);
		String date = Stream.of(text.split(" ")).filter(f -> p.matcher(f).find()).findFirst().get()
				.replaceAll("[a-zA-Z]", "").trim();

		System.out.println("Test fecha: " + date);
	}

	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9]){1}(.))";

	@Test
	void mustReturnSupplyItemList() throws Exception {
		Pattern pCode = Pattern.compile(itemCodeRegex);
		Pattern pProgCat = Pattern.compile(itemProgCatRegex);
		Pattern pQuantity = Pattern.compile(itemQuantityRegex);
		List<String> strItems = Stream.of(arrTextSplitN).filter(f -> pCode.matcher(f).find())
				.collect(Collectors.toList());
		ArrayList<SupplyItem> itemsArray = new ArrayList<>();
		List<SupplyItem> itemsList = strItems.stream().map(item -> {
			SupplyItem supplyItem = new SupplyItem();
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

			});

			return supplyItem;
		}).toList();
		itemsList.forEach(e -> System.out.println(e));
	}

	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile("^(?=.*([0-9])*(,)([0-9]){3}(.)([0-9]){2})", Pattern.CASE_INSENSITIVE);
		// Matcher m = p2.matcher("P.V. Nro. 00007 -");
		// System.out.println("test: "+arrTextSplitN[2]);
		assertTrue(p2.matcher("4,663.00").find());
	}

}
