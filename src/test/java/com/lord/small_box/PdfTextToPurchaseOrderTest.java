package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
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
		text = pdfToStringUtils.pdfToString("oc-365.pdf");
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
		System.err.println("Order Date: " + purchaseOrderDto.getDate());
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
		System.out.println("ExecUnit = " + executerInut);
		return executerInut;

	}

	private final String dependencyRegex = "^(?=.*(dependencia))";

	private String getDependency(String[] arrText) {
		Pattern pDependency = Pattern.compile(dependencyRegex, Pattern.CASE_INSENSITIVE);
		String dependency = Stream.of(arrText).filter(f -> pDependency.matcher(f).find())
				.map(m -> m.substring(m.trim().indexOf(":") + 1, m.length() - 1)).findFirst().get()
				.replace("- Secretarķa", "").replace("Secretaria", "").trim();
		return dependency;
	}

	private OrganizationDto getOrganization(String target) {
		String orgFinderRegex = "(?=.*(" + target + "))";
		Pattern pOrgFinderRegex = Pattern.compile(orgFinderRegex, Pattern.CASE_INSENSITIVE);

		OrganizationDto findedOrgDto = organizationService.findAll().stream()
				.filter(f -> pOrgFinderRegex.matcher(f.getOrganizationName()).find()).findFirst()
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));
		System.out.println("FInded org: " + findedOrgDto.getOrganizationName());
		return findedOrgDto;
	}

	private int getPurchaseOrderNumber(String[] arrText) {
		return Integer.parseInt(Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst().get()
				.replaceAll("[\\D]", "").strip());
	}
	
	private final String financingSourceRegex = "(?=.*(fuente de financiamiento))";
	private String getFinancingSource(String[] arrText) {
		Pattern pFinancingSource = Pattern.compile(financingSourceRegex,Pattern.CASE_INSENSITIVE);
		String financingSourceLine = Stream.of(arrText).filter(f -> pFinancingSource.matcher(f).find()).findFirst()
				.orElse("No encontrado");
		return financingSourceLine.substring(financingSourceLine.indexOf(":")+1,financingSourceLine.indexOf(":")+5);
				
	}

	private BigDecimal getPurchaseTotal(String[] arrText) {
		return new BigDecimal(Stream.of(arrText).filter(f -> f.toLowerCase().contains("total:")).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace(":", "").replace("$", "").replace(".", "").replace(",", ".")
				.strip());
	}

	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";

	private List<PurchaseOrderItemDto> getItems(String[] arrText) {
		Pattern pItemCode = Pattern.compile(itemCodeRegex);
		Pattern pItemQuantity = Pattern.compile(itemQuantityRegex);
		Pattern pProgCat = Pattern.compile(itemProgCatRegex);
		Pattern pUnitPrice = Pattern.compile(itemUnitPrice);

		List<String> itemsText = Stream.of(arrText).filter(f -> pItemCode.matcher(f).find())
				.collect(Collectors.toList());
		itemsText.forEach(e -> System.err.println(e));
		return itemsText.stream().map(item -> {
			PurchaseOrderItemDto purchaseOrderItemDto = new PurchaseOrderItemDto();
			String[] arrItems = item.split(" ");
			String code = Stream.of(arrItems).filter(f -> pItemCode.matcher(f).find()).findFirst()
					.orElse("No encontrado");
			code.strip();
			purchaseOrderItemDto.setCode(code);

			purchaseOrderItemDto
					.setQuantity(Integer.parseInt(Stream.of(arrItems).filter(f -> pItemQuantity.matcher(f).find())
							.map(m -> m.substring(0, m.indexOf(","))).findFirst().orElse("0")));

			String measureUnit = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).findFirst()
					.orElse("No encontrado");
			if (measureUnit.equalsIgnoreCase("cada")) {
				measureUnit = measureUnit + "-UNO";
			}
			purchaseOrderItemDto.setMeasureUnit(measureUnit);

			String progCat = Stream.of(arrItems).filter(f -> pProgCat.matcher(f).find()).findFirst()
					.orElse("No encontrado");
			progCat.strip();
			purchaseOrderItemDto.setProgramaticCat(progCat);

			String itemDetails = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).skip(1)
					.map(m -> m.replaceAll("[0-9\\W]", "")).collect(Collectors.joining("-"));
			purchaseOrderItemDto.setItemDetail(itemDetails);

			purchaseOrderItemDto
					.setUnitCost(new BigDecimal(Stream.of(arrItems).filter(f -> pUnitPrice.matcher(f).find())
							.map(m -> m.replace(".", "").replace(",", ".")).findFirst().orElse("0")));

			purchaseOrderItemDto.setTotalEstimatedCost(new BigDecimal(
					item.substring(item.lastIndexOf("$") + 1).replace(".", "").replace(",", ".").strip()));

			return purchaseOrderItemDto;

		}).toList();
	}

	private final String strDateV2 = "^(?=.*(?=.*[0-9]{2})*(?=.*[/]{1})){2}(?=.*[0-9]{2,4})";

	private Calendar getDate(String text) {
		Pattern pDate = Pattern.compile(strDateV2);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = Stream.of(text).filter(f -> pDate.matcher(f).find()).findFirst().get().replaceAll("[a-zA-Z]", "")
				.replace("/", "-").strip();
		try {
			cal.setTime(sdf.parse(date));
			return cal;
		} catch (ParseException e) {
			throw new RuntimeException("Error al parsear la fecha");
		}
	}

	@Test
	void patternTest() throws Exception {
		Pattern p2 = Pattern.compile("^(?=.*([0-9].)*(,)([0-9]){5})", Pattern.CASE_INSENSITIVE);
		assertTrue(p2.matcher("10.600,00000").find());
	}
}
