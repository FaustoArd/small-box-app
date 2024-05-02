package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.ClassNameComparator;
import org.assertj.core.util.NaturalOrderComparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class PdfTextToSupplyTest {

	@Autowired
	private PdfToStringUtils pdfToStringUtils;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationResponsibleRepository organizationResponsibleRepository;

	private List<String> measureUnits = List.of("cada", "kilogramo", "unidad");

	private String text;
	private List<String> supplyPdfList;
	private String[] arrTextSplitPageEnd;
	private String[] arrTextSplitN;

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

		OrganizationResponsible beban = new OrganizationResponsible();
		lagunas.setName("Rodolfo");
		lagunas.setLastname("Beban");
		OrganizationResponsible savedBeban = organizationResponsibleRepository.save(beban);

		OrganizationResponsible peuchele = new OrganizationResponsible();
		peuchele.setName("Ruben");
		peuchele.setLastname("Peuchele");
		OrganizationResponsible savedPeuchele = organizationResponsibleRepository.save(peuchele);

		OrganizationResponsible melco = new OrganizationResponsible();
		peuchele.setName("Carlos");
		peuchele.setLastname("Melconian");
		OrganizationResponsible savedMelco = organizationResponsibleRepository.save(peuchele);

		Organization org1 = new Organization();
		org1.setOrganizationName("Secretaria de Desarrollo Social");
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

		Organization adMAyores = new Organization();
		adMAyores.setOrganizationName("DIRECCION DE ADULTOS MAYORES");
		adMAyores.setOrganizationNumber(6);
		adMAyores.setMaxAmount(new BigDecimal(100000));
		adMAyores.setMaxRotation(12);
		adMAyores.setResponsible(savedBeban);

		Organization DDHPS = new Organization();
		DDHPS.setOrganizationName("DIRECCION DE DESARROLLO HUMANO Y PROMOCIÓN SOCIAL");
		DDHPS.setOrganizationNumber(6);
		DDHPS.setMaxAmount(new BigDecimal(100000));
		DDHPS.setMaxRotation(12);
		DDHPS.setResponsible(savedPeuchele);

		Organization DDNI = new Organization();
		DDNI.setOrganizationName("DIRECCION DE NIÑEZ");
		DDNI.setOrganizationNumber(6);
		DDNI.setMaxAmount(new BigDecimal(100000));
		DDNI.setMaxRotation(12);
		DDNI.setResponsible(savedMelco);

		Organization secDesSocial = organizationService.save(org1);
		Organization dirAdmDesp = organizationService.save(org2);
		organizationService.save(org3);
		organizationService.save(org4);
		organizationService.save(org5);
		organizationService.save(adMAyores);
		organizationService.save(DDHPS);
		organizationService.save(DDNI);
		text = pdfToStringUtils.pdfToString("sum-671.pdf");
		arrTextSplitPageEnd = text.split("PageEnd");
		arrTextSplitN = text.split("\\n");
		// supplyPdfList.forEach(e -> System.out.println(e));
		for (String s : arrTextSplitN) {
			System.out.println(s);
		}
	}

	@Test
	void mustReturnSupplyDto() throws Exception {
		SupplyDto supplyDto = new SupplyDto();
		supplyDto.setSupplyNumber(getSupplyNumber(arrTextSplitN));
		supplyDto.setDate(getDate(text));
		supplyDto.setSupplyItems(getSupplyItemList(arrTextSplitN));
		supplyDto.setEstimatedTotalCost(getEstimatedTotal(arrTextSplitN));
		supplyDto.setDependencyApplicant(getApplicant(arrTextSplitN));
		/*
		 * Optional<OrganizationDto> optApplicantDto =
		 * Optional.of(getApplicant(arrTextSplitN)); if (optApplicantDto.isPresent()) {
		 * supplyDto.setDependencyApplicant(optApplicantDto.get().getOrganizationName())
		 * ;
		 * supplyDto.setDependencyApplicantOrganizationId(optApplicantDto.get().getId())
		 * ; }
		 */
		System.out.println("Applicant: " + supplyDto.getDependencyApplicant());
		System.out.println("Estimated: " + supplyDto.getEstimatedTotalCost());
		System.out.println("TEST: " + supplyDto.getSupplyNumber());
		System.out.println("Date: " + supplyDto.getDate().getTime());
		supplyDto.getSupplyItems().forEach(e -> System.out.println(e.getCode()));
		supplyDto.getSupplyItems().forEach(e -> System.out.println("quant: " + e.getQuantity()));
		supplyDto.getSupplyItems().forEach(e -> System.out.println("measure unit: " + e.getMeasureUnit()));
		/*
		 * assertEquals(supplyDto.getSupplyItems().get(0).getMeasureUnit(),
		 * "KILOGRAMO");
		 * assertEquals(supplyDto.getSupplyItems().get(1).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(2).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(3).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(4).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(5).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(6).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(7).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(8).getMeasureUnit(), "CADA-UNO");
		 * assertEquals(supplyDto.getSupplyItems().get(9).getMeasureUnit(), "CADA-UNO");
		 */
	}



	
	private String getApplicant(String[] arrText) {

		String applicant = Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst()
				.map(m -> m.substring(m.indexOf("O") + 1, m.lastIndexOf("M") - 1).replace("Secretaría de", "")
						.replace("Dirección de", "").replace("Subsecretaría de", "").trim())
				.get();

		return applicant;
	}

	private BigDecimal getEstimatedTotal(String[] arrText) {

		return new BigDecimal(Stream.of(arrText).filter(f -> f.toLowerCase().contains("total")).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace(":", "").replace("$", "").replace(".", "").replace(",", ".")
				.strip());
	}

	private final String supplyNumberTitleRegex = "(SOLICITUD DE PEDIDO Nº)";

	private int getSupplyNumber(String[] arrText) {

		Pattern p = Pattern.compile(supplyNumberTitleRegex);
		String number = Stream.of(arrText).filter(f -> p.matcher(f).find()).map(m -> m.substring(24, 28))
				.map(m -> m.replaceAll("[a-zA-Z\\D]", "")).collect(Collectors.joining(""));

		return Integer.parseInt(number);
	}

	private final String strDateV2 = "^(?=.*([0-9]{2})*([/]{1})){2}([0-9]{2,4})";

	private Calendar getDate(String text) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Pattern p = Pattern.compile(strDateV2);
		String date = Stream.of(text.split(" ")).filter(f -> p.matcher(f).find()).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace("/", "-").trim();

		try {
			cal.setTime(sdf.parse(date));
			return cal;
		} catch (ParseException ex) {
			throw new RuntimeException("Error al parsear la fecha");
		}
	}

	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemProgCatRegex = "^(([0-9]){1,2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemQuantityRegex = "([0-9]*[,]{1})?([0-9]{1,3}[.]{1}[0-9]{2}){1}";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	// private final String itemMeasureUnitRegex ="(?=.*(cada)?(kilogramo)?)";

	Pattern pCode = Pattern.compile(itemCodeRegex);
	Pattern pProgCat = Pattern.compile(itemProgCatRegex);
	Pattern pQuantity = Pattern.compile(itemQuantityRegex);
	Pattern pUnitPrice = Pattern.compile(itemUnitPrice);

	private List<SupplyItemDto> getSupplyItemList(String[] arrText) {

		List<String> strItems = Stream.of(arrText).filter(f -> pCode.matcher(f).find()).collect(Collectors.toList());
		return strItems.stream().map(item -> {
			SupplyItemDto supplyItemDto = new SupplyItemDto();
			String[] arrItems = item.split(" ");

			supplyItemDto.setCode(getItemCode(arrItems));

			supplyItemDto.setQuantity(getItemQuantity(arrItems));

			supplyItemDto.setMeasureUnit(getItemMeasureUnit(arrItems));

			supplyItemDto.setProgramaticCat(getItemProgramaticCat(arrItems));

			supplyItemDto.setItemDetail(getItemDetails(arrItems));

			supplyItemDto.setUnitCost(getItemUnitCost(arrItems));

			supplyItemDto.setTotalEstimatedCost(getItemTotalEstimatedCost(item));

			return supplyItemDto;

		}).toList();
	}

	private String getItemCode(String[] arrItems) {
		return Stream.of(arrItems).filter(f -> pCode.matcher(f).find()).findFirst().get().strip();
	}

	private int getItemQuantity(String[] arrItems) {
		String quantityResult = Stream.of(arrItems).filter(f -> pQuantity.matcher(f).matches()).findFirst().get();
		if (quantityResult.contains(",")) {
			quantityResult = quantityResult.replace(",", "");
			return new BigDecimal(quantityResult).intValue();
		} else {
			return new BigDecimal(quantityResult).intValue();
		}
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

	private String getItemDetails(String[] arrItems) {
		return Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).skip(1).map(m -> m.replaceAll("[0-9\\W]", ""))
				.collect(Collectors.joining("-"));
	}

	private BigDecimal getItemUnitCost(String[] arrItems) {
		return new BigDecimal(Stream.of(arrItems).filter(f -> pUnitPrice.matcher(f).find())
				.map(m -> m.replace(".", "").replace(",", ".")).findFirst().get());
	}

	private BigDecimal getItemTotalEstimatedCost(String item) {
		return new BigDecimal(item.substring(item.lastIndexOf("$") + 1).replace(".", "").replace(",", ".").strip());
	}
}
