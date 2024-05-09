package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.mock.web.MockMultipartFile;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.exceptions.TextFileInvalidException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PdfTextToPurchaseOrderTest {

	@Autowired
	private PdfToStringUtils pdfToStringUtils;
	private String text;
	private List<String> supplyPdfList;
	private String[] arrTextSplitPageEnd;
	private String[] arrTextSplitN;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationResponsibleRepository organizationResponsibleRepository;

	private final String supplyNumberRegex = "^(?=.*[0-9]{1,4})";

	@BeforeAll
	void setup() throws Exception {
		OrganizationResponsible reyes = new OrganizationResponsible();
		reyes.setName("Blasa");
		reyes.setLastname("Reyes");
		OrganizationResponsible savedReyes = organizationResponsibleRepository.save(reyes);
		OrganizationResponsible pierpa = new OrganizationResponsible();
		pierpa.setName("Roxana");
		pierpa.setLastname("Pierpaoli");
		OrganizationResponsible savedPierpa = organizationResponsibleRepository.save(pierpa);
		OrganizationResponsible fabi = new OrganizationResponsible();
		fabi.setName("Fabian");
		fabi.setLastname("Yanes");
		OrganizationResponsible saveFabi = organizationResponsibleRepository.save(fabi);
		OrganizationResponsible iasil = new OrganizationResponsible();
		iasil.setName("Luciana");
		iasil.setLastname("Iasil");
		OrganizationResponsible saveIasil = organizationResponsibleRepository.save(iasil);

		OrganizationResponsible lagunas = new OrganizationResponsible();
		lagunas.setName("Analia");
		lagunas.setLastname("Lagunas");
		OrganizationResponsible savedLagunas = organizationResponsibleRepository.save(lagunas);

		Organization org1 = new Organization();
		org1.setOrganizationName("Secretaria de desarrollo social");
		org1.setOrganizationNumber(1);
		org1.setMaxRotation(12);
		org1.setMaxAmount(new BigDecimal(180000));
		org1.setResponsible(savedPierpa);

		Organization org2 = new Organization();
		org2.setOrganizationName("Direccion de administracion y despacho");
		org2.setOrganizationNumber(2);
		org2.setMaxRotation(12);
		org2.setMaxAmount(new BigDecimal(80000));
		org2.setResponsible(savedReyes);

		Organization org3 = new Organization();
		org3.setOrganizationName("Direccion de logistica");
		org3.setOrganizationNumber(3);
		org3.setMaxRotation(12);
		org3.setMaxAmount(new BigDecimal(45000));
		org3.setResponsible(saveFabi);

		Organization org4 = new Organization();
		org4.setOrganizationName("Subsecretaria de Politicas Socio Comunitarias");
		org4.setResponsible(saveIasil);
		org4.setMaxRotation(12);
		org4.setMaxAmount(new BigDecimal(100000));
		org4.setOrganizationNumber(4);

		Organization org5 = new Organization();
		org5.setOrganizationName("Dirección de Reinserción Social");
		org5.setOrganizationNumber(5);
		org5.setMaxAmount(new BigDecimal(100000));
		org5.setMaxRotation(12);
		org5.setResponsible(savedLagunas);

		Organization secDesSocial = organizationService.save(org1);
		Organization dirAdmDesp = organizationService.save(org2);
		organizationService.save(org3);
		organizationService.save(org4);
		organizationService.save(org5);
		
		// Stream.of(arrTextSplitPageEnd).forEach(e -> System.out.println(e));
		// supplyPdfList.forEach(e -> System.out.println(e));
	
	}

	@Test
	void mustReturnPurchaseOrder() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "oc-760.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\oc-760.pdf").getContentAsByteArray());
		String text = pdfToStringUtils.pdfToString(file.getBytes());
		arrTextSplitPageEnd = text.split("PageEnd");
		arrTextSplitN = text.split("\\n");
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setDate(getDate(text));
		purchaseOrderDto.setOrderNumber(getPurchaseOrderNumber(arrTextSplitN));
		purchaseOrderDto.setFinancingSource(getFinancingSource(arrTextSplitN));
		purchaseOrderDto.setItems(getItems(arrTextSplitN));
		purchaseOrderDto.setPurchaseOrderTotal(getPurchaseTotal(arrTextSplitN));
		purchaseOrderDto.setExecuterUnit(getExecuterUnit(arrTextSplitN));
		purchaseOrderDto.setDependency(getDependency(arrTextSplitN));

		System.err.println("Order Number: " + purchaseOrderDto.getOrderNumber());
		System.err.println("Financing source: " + purchaseOrderDto.getFinancingSource());
		System.err.println("Order TOTAL: " + purchaseOrderDto.getPurchaseOrderTotal());
		System.err.println("Order Date: " + purchaseOrderDto.getDate().get(Calendar.DATE) + "-"
				+ purchaseOrderDto.getDate().get(Calendar.MONTH) + "-" + purchaseOrderDto.getDate().get(Calendar.YEAR));
		System.err.println("Executer Unit: " + purchaseOrderDto.getExecuterUnit());
		System.err.println("Dependency: " + purchaseOrderDto.getDependency());
		purchaseOrderDto.getItems().forEach(e -> System.out.println(e.getQuantity()));
		assertThat(purchaseOrderDto.getPurchaseOrderTotal()).isGreaterThan(new BigDecimal(0));
		assertThat(purchaseOrderDto.getItems().stream().mapToDouble(d -> d.getTotalEstimatedCost().doubleValue()).sum())
				.isEqualTo(purchaseOrderDto.getPurchaseOrderTotal().doubleValue());

	}

	private final String executerUnitRegex = "^(?=.*(unidad ejecutora))";

	private String getExecuterUnit(String[] arrText) {

		Pattern pExecUnit = Pattern.compile(executerUnitRegex, Pattern.CASE_INSENSITIVE);
		String executerInut = Stream.of(arrText).filter(f -> pExecUnit.matcher(f).find())
				.map(m -> m.substring(m.indexOf(":") + 1, m.lastIndexOf(":") - 5)).findFirst().get()
				.replace("- Secretarķa", "").trim();
		return executerInut;

	}

	private final String financingSourceRegex = "(?=.*(fuente de financiamiento))";

	private String getFinancingSource(String[] arrText) {

		Pattern pFinancingSource = Pattern.compile(financingSourceRegex, Pattern.CASE_INSENSITIVE);
		String financingSourceLine = Stream.of(arrText).filter(f -> pFinancingSource.matcher(f).find()).findFirst()
				.orElse("No encontrado");
		return financingSourceLine.substring(financingSourceLine.indexOf(":") + 1, financingSourceLine.indexOf(":") + 5)
				.trim();

	}

	private final String dependencyRegex = "^(?=.*(dependencia))";

	private String getDependency(String[] arrText) {

		Pattern pDependency = Pattern.compile(dependencyRegex, Pattern.CASE_INSENSITIVE);
		String dependency = Stream.of(arrText).filter(f -> pDependency.matcher(f).find())
				.map(m -> m.substring(m.trim().indexOf(":") + 1, m.length() - 1)).findFirst().get().trim();
		return dependency;
	}

	private int getPurchaseOrderNumber(String[] arrText) {

		String orderNumber =  Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst().get().replaceAll("[\\D]", "")
				.strip();
		return Integer.parseInt(orderNumber); 
	}

	private BigDecimal getPurchaseTotal(String[] arrText) {

		return new BigDecimal(Stream.of(arrText).filter(f -> f.toLowerCase().contains("total:")).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace(":", "").replace("$", "").replace(".", "").replace(",", ".")
				.strip());
	}

	// REGEX to find items data.
	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3})";
	private final String itemProgCatRegex = "^(([0-9]){1,2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	private final Pattern pItemCode = Pattern.compile(itemCodeRegex);
	private final Pattern pItemQuantity = Pattern.compile(itemQuantityRegex);
	private final Pattern pProgCat = Pattern.compile(itemProgCatRegex);
	private final Pattern pUnitPrice = Pattern.compile(itemUnitPrice);

	// This method find all the purchase order items.
	private List<PurchaseOrderItemDto> getItems(String[] arrText) {

		// This list contains all lines that match the item code REGEX
		List<String> itemsText = Stream.of(arrText).filter(f -> pItemCode.matcher(f).find())
				.collect(Collectors.toList());

		// Iterate the list and save each item element in a new PurchaseOrderItem
		// object.

		return itemsText.stream().map(item -> {
			PurchaseOrderItemDto purchaseOrderItem = new PurchaseOrderItemDto();
			String[] arrItems = item.split(" ");
			purchaseOrderItem.setCode(getItemCode(arrItems));
			purchaseOrderItem.setQuantity(getItemQuantity(arrItems));
			purchaseOrderItem.setMeasureUnit(getItemMeasureUnit(arrItems));
			purchaseOrderItem.setProgramaticCat(getItemProgramaticCat(arrItems));
			purchaseOrderItem.setItemDetail(getItemDetail(arrItems));
			purchaseOrderItem.setUnitCost(getUnitCost(arrItems));
			purchaseOrderItem.setTotalEstimatedCost(getItemTotalCost(item));
			return purchaseOrderItem;
		}).toList();
	}

	private String getItemCode(String[] arrItems) {

		return Stream.of(arrItems).filter(f -> pItemCode.matcher(f).find()).findFirst().get().strip();
	}

	private int getItemQuantity(String[] arrItems) {

		String strQuantity = Stream.of(arrItems).filter(f -> pItemQuantity.matcher(f).find())
				.map(m -> m.substring(0, m.indexOf(",")).trim()).findFirst().get();
		if (strQuantity.contains("."))
			;
		strQuantity = strQuantity.replace(".", "");
		int quantity = Integer.parseInt(strQuantity);
		System.out.println("Quantitry: " + quantity);
		return quantity;

		/*
		 * return Integer.parseInt(Stream.of(arrItems).filter(f ->
		 * pItemQuantity.matcher(f).find()) .map(m -> m.substring(0,
		 * m.indexOf(","))).findFirst().get());
		 */
	}

	private String getItemMeasureUnit(String[] arrItems) {

		String measureUnitResult = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).findFirst().get();
		if (measureUnitResult.equalsIgnoreCase("cada")) {
			measureUnitResult = measureUnitResult + "-UNO";
		}
		return measureUnitResult;
	}

	private String getItemProgramaticCat(String[] arrItems) {

		return Stream.of(arrItems).filter(f -> pProgCat.matcher(f).find()).findFirst().get().strip();
	}

	private String getItemDetail(String[] arrItems) {

		String itemDetail = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).skip(1)
				.map(m -> m.replaceAll("[0-9\\W]", "")).collect(Collectors.joining("-"));
		if (itemDetail.startsWith("UNO-")) {
			itemDetail = itemDetail.substring(itemDetail.indexOf("-") + 1, itemDetail.length() - 1);
		}
		return itemDetail;
	}

	private BigDecimal getUnitCost(String[] arrItems) {

		return new BigDecimal(Stream.of(arrItems).filter(f -> pUnitPrice.matcher(f).find())
				.map(m -> m.replace(".", "").replace(",", ".")).findFirst().get());
	}

	private BigDecimal getItemTotalCost(String item) {

		return new BigDecimal(item.substring(item.lastIndexOf("$") + 1).replace(".", "").replace(",", ".").strip());
	}

	private final String strDateV2 = "^(?=.*(?=.*[0-9]{2})*(?=.*[/]{1})){2}(?=.*[0-9]{2,4})";

	private final String strDateFinder = "^(?=.*(fecha))";

	private Calendar getDate(String text) {
		Pattern pDateFinder = Pattern.compile(strDateFinder, Pattern.CASE_INSENSITIVE);
		Pattern pDate = Pattern.compile(strDateV2);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = Stream.of(text.split(" "))
				.filter(f -> pDate.matcher(f).find())
				.skip(1)
				.findFirst().orElseThrow(()-> new TextFileInvalidException("El archivo no es compatible con una orden de compra"));
				date = date.replaceAll("[a-zA-Z]", "").replace("/", "-").strip();
		/*
		 * .map(m -> m.substring(m.indexOf(":")+1,m.length()-1).replace("/",
		 * "-").strip()).get();
		 */
		System.err.println("New Date: " + date);
		/*
		 * String date = Stream.of(text).filter(f ->
		 * pDate.matcher(f).find()).findFirst().get().replaceAll("[a-zA-Z]", "")
		 * .replace("/", "-").strip();
		 */
		try {
			cal.setTime(sdf.parse(date));

			return cal;
		} catch (ParseException ex) {
			throw new TextFileInvalidException("Error al parsear la fecha.",ex);
		}
	}
}
