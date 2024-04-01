package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
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
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private OrganizationResponsibleRepository organizationResponsibleRepository;
	
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
		Organization secDesSocial = organizationService.save(org1);
		Organization dirAdmDesp = organizationService.save(org2);
		organizationService.save(org3);
		organizationService.save(org4);
		organizationService.save(org5);
		text = pdfToStringUtils.pdfToString("sum-551");
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
		supplyDto.setSupplyNumber(mustReturnSupplyNumber(arrTextSplitN));
		supplyDto.setDate(mustReturnDate(text));
		supplyDto.setSupplyItems(mustReturnSupplyItemList(arrTextSplitN));
		supplyDto.setEstimatedTotalCost(getEstimatedTotal(arrTextSplitN));
		Optional<OrganizationDto> optApplicantDto = Optional.of(getApplicant(arrTextSplitN));
		if(optApplicantDto.isPresent()){
			supplyDto.setDependencyApplicant(optApplicantDto.get().getOrganizationName());
			supplyDto.setDependencyApplicantOrganizationId(optApplicantDto.get().getId());
		}
		System.out.println("Applicant: "+ supplyDto.getDependencyApplicant());
		System.out.println("Applicant ID: "+ supplyDto.getDependencyApplicantOrganizationId());
		System.out.println("Estimated: "+supplyDto.getEstimatedTotalCost());
		System.out.println("TEST: " + supplyDto.getSupplyNumber());
		System.out.println("TEST: " + supplyDto.getDate().getTime());
		supplyDto.getSupplyItems().forEach(e -> System.out.println(e.getCode()));
	}
	
	private OrganizationDto getApplicant(String[] arrText) {
		String applicant =  Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst()
				.map(m -> m.substring(m.indexOf("O")+1, m.lastIndexOf("M")-1)
						.replace("Secretaría de", "").replace("Dirección de", "")
						.replace("Subsecretaría de", "").trim()).get();
		System.err.println("getApplicant: " + applicant);
		return getOrganization(applicant);
	}
	
	private OrganizationDto getOrganization(String applicant) {
		String orgFinderRegex = "(?=.*(" + applicant + "))";
		Pattern pOrgFinderRegex = Pattern.compile(orgFinderRegex, Pattern.CASE_INSENSITIVE);
		organizationService.findAll().forEach(e -> System.out.println(e.getOrganizationName()));
		OrganizationDto findedOrgDto = organizationService.findAll().stream()
				.filter(f -> pOrgFinderRegex.matcher(f.getOrganizationName()).find()).findFirst()
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la organizacion"));
		System.out.println("FInded org: " + findedOrgDto.getOrganizationName());
		return findedOrgDto;
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

	
	private List<SupplyItemDto> mustReturnSupplyItemList(String[] arrText) throws Exception {
		Pattern pCode = Pattern.compile(itemCodeRegex);
		Pattern pProgCat = Pattern.compile(itemProgCatRegex);
		Pattern pQuantity = Pattern.compile(itemQuantityRegex);
		Pattern pUnitPrice = Pattern.compile(itemUnitPrice);
		//Pattern pMesaureUnit = Pattern.compile(itemMeasureUnitRegex,Pattern.CASE_INSENSITIVE);
		List<String> strItems = Stream.of(arrText).filter(f -> pCode.matcher(f).find())
				.collect(Collectors.toList());
		return  strItems.stream().map(item -> {
			SupplyItemDto supplyItemDto = new SupplyItemDto();
			supplyItemDto.setItemDetail(item.replaceAll("([0-9]*\\W)", " ").trim());
			supplyItemDto.setTotalEstimatedCost(new BigDecimal(item.substring(item.indexOf("$")+1)
					.replace(".", "").replace(",", ".").strip()));
			ListIterator<String> list = Stream.of(item.split(" ")).toList().listIterator();
			list.forEachRemaining(i -> {
				if (pCode.matcher(i).find()) {
					supplyItemDto.setCode(i);
				}
				if (pProgCat.matcher(i).matches()) {
					supplyItemDto.setProgramaticCat(i);
				}
				if (pQuantity.matcher(i).find()) {
					supplyItemDto.setQuantity(Integer.parseInt(i));
				}
				if(pUnitPrice.matcher(i).find()) {
					i= i.replace(".", "");
					i = i.replace(",", ".");
							
					supplyItemDto.setUnitCost(new BigDecimal(i));
				}
				if(i.toLowerCase().contains("cada")) {
					i = i + " UNO";
					supplyItemDto.setMeasureUnit(i);
				}if( i.toLowerCase().contains("kilogramo")) {
					supplyItemDto.setMeasureUnit(i);
				}

			});

			return supplyItemDto;
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
