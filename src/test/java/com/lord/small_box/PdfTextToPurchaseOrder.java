package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PdfTextToPurchaseOrder {

	@Autowired
	private PdfToStringUtils pdfToStringUtils;
	private String text;
	private List<String> supplyPdfList;
	private String[] arrTextSplitPageEnd;
	private String[] arrTextSplitN;

	private final String supplyNumberRegex = "^(?=.*[0-9]{1,4})";
	
	

	@BeforeAll
	void setup() throws Exception {
		text = pdfToStringUtils.pdfToReceipt("oc-367");
		arrTextSplitPageEnd = text.split("PageEnd");
		arrTextSplitN = text.split("\\n");
		// Stream.of(arrTextSplitPageEnd).forEach(e -> System.out.println(e));
		// supplyPdfList.forEach(e -> System.out.println(e));
		for (String s : arrTextSplitN) {
			System.out.println(s);
		}
	}

	@Test
	void mustReturnPurchaseOrder() throws Exception {

		PurchaseOrder purchaseOrder = PurchaseOrder.builder()
				.orderNumber(Integer.parseInt(getPurchaseOrderNumber(arrTextSplitN)))
				.date(getDate(text))
				.items(getItems(arrTextSplitN))
				.purchaseOrderTotal(getPurchaseTotal(arrTextSplitN))
				.build();
		
		System.err.println("Order Number:"+ purchaseOrder.getOrderNumber());
		System.err.println("Order TOTAL: " + purchaseOrder.getPurchaseOrderTotal());
		System.err.println("Order Date: " + purchaseOrder.getDate());
		System.err.println("Purchase order: " + purchaseOrder.getItems());
	}
	
	private String getPurchaseOrderNumber(String[] arrText) {
		return Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst().get().replaceAll("[\\D]", "").strip();
	}
	
	private BigDecimal getPurchaseTotal(String[] arrText) {
		return new BigDecimal(Stream.of(arrText)
				.filter(f -> f.toLowerCase().contains("total:"))
				.findFirst().get()
				.replaceAll("[a-zA-Z]", "")
				.replace(":", "")
				.replace("$", "")
				.replace(".", "")
				.replace(",", ".")
				.strip());
	}

	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	
	private List<PurchaseOrderItem> getItems(String[] arrText) {
		Pattern pItemCode = Pattern.compile(itemCodeRegex);
		Pattern pItemQuantity = Pattern.compile(itemQuantityRegex);
		Pattern pProgCat = Pattern.compile(itemProgCatRegex);
		Pattern pUnitPrice = Pattern.compile(itemUnitPrice);
		
		List<String> itemsText = Stream.of(arrText).filter(f -> pItemCode.matcher(f).find()).collect(Collectors.toList());
		itemsText.forEach(e -> System.out.println(e));
		return itemsText.stream().map(item -> {
			PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
			String[] arrItems = item.split(" ");
			purchaseOrderItem
					.setCode(Stream.of(arrItems).filter(f -> pItemCode.matcher(f).find()).findFirst().get().strip());
			
			purchaseOrderItem
					.setQuantity(Integer.parseInt(Stream.of(arrItems).filter(f -> pItemQuantity.matcher(f).find())
							.map(m -> m.substring(0, m.indexOf(","))).findFirst().get()));
			
			String quantityResult = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).findFirst().get();
			if (quantityResult.equalsIgnoreCase("cada")) {
				quantityResult = quantityResult + "-UNO";
			}
			purchaseOrderItem.setMeasureUnit(quantityResult);
			
			purchaseOrderItem.setProgramaticCat(Stream.of(arrItems)
					.filter(f -> pProgCat.matcher(f).find())
					.findFirst().get().strip());
			
			purchaseOrderItem.setItemDetail(Stream.of(arrItems)
					.filter(f -> f.matches("([a-zA-Z]*)")).skip(1)
					.map(m -> m.replaceAll("[0-9\\W]", ""))
					.collect(Collectors.joining("-")));
			
			purchaseOrderItem.setUnitCost(new BigDecimal(Stream.of(arrItems).filter(f -> pUnitPrice.matcher(f).find())
					.map(m -> m.replace(".", "").replace(",", ".")).findFirst().get()));
			
			purchaseOrderItem.setTotalEstimatedCost(new BigDecimal(item.substring(item.lastIndexOf("$")+1)
					.replace(".", "").replace(",", ".").strip()));
			return purchaseOrderItem;

		}).toList();
	}

	private final String strDateV2 = "^(?=.*(?=.*[0-9]{2})*(?=.*[/]{1})){2}(?=.*[0-9]{2,4})";

	private Calendar getDate(String text) {
		Pattern pDate = Pattern.compile(strDateV2);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = Stream.of(text).filter(f -> pDate.matcher(f).find()).findFirst().get().replaceAll("[a-zA-Z]", "")
				.replace("/", "-").strip();
		// System.err.println("Date: "+date);
		try {
			cal.setTime(sdf.parse(date));

			// System.err.println("Date:"+date);
			return cal;
		} catch (ParseException e) {
			throw new RuntimeException("Error al parsear la fecha");
		}
	}

	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile(strDateV2, Pattern.CASE_INSENSITIVE);
		// Matcher m = p2.matcher("P.V. Nro. 00007 -");
		// System.out.println("test: "+arrTextSplitN[2]);
		assertTrue(p2.matcher("19/02/2024").find());
	}
}
