package com.lord.small_box;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";

	@BeforeAll
	void setup() throws Exception {
		text = pdfToStringUtils.pdfToReceipt("oc-365");
		arrTextSplitPageEnd = text.split("PageEnd");
		arrTextSplitN = text.split("\\n");
		//Stream.of(arrTextSplitPageEnd).forEach(e -> System.out.println(e));
		// supplyPdfList.forEach(e -> System.out.println(e));
		for (String s : arrTextSplitN) {
			System.out.println(s);
		}
	}
	@Test
	void mustReturnPurchaseOrder()throws Exception{
		
	}
}
