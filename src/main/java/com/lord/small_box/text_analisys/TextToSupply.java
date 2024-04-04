package com.lord.small_box.text_analisys;

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
import org.springframework.stereotype.Component;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.services.OrganizationService;


@Component
public class TextToSupply {
	
	public SupplyDto textToSupply(String text,OrganizationService organizationService) {
		SupplyDto supplyDto = new SupplyDto();
		String[] arrTextSplitN = text.split("\\n");
		supplyDto.setSupplyNumber(mustReturnSupplyNumber(arrTextSplitN));
		supplyDto.setDate(mustReturnDate(text));
		supplyDto.setSupplyItems(mustReturnSupplyItemList(arrTextSplitN));
		supplyDto.setEstimatedTotalCost(getEstimatedTotal(arrTextSplitN));
		supplyDto.setDependencyApplicant(getApplicant(arrTextSplitN));
		System.out.println("Applicant: "+ supplyDto.getDependencyApplicant());
		System.out.println("Estimated: "+supplyDto.getEstimatedTotalCost());
		System.out.println("TEST: " + supplyDto.getSupplyNumber());
		System.out.println("TEST: " + supplyDto.getDate().getTime());
		System.out.println("TEST: " + supplyDto.getSupplyItems());
		return supplyDto;
	}
	
	private String getApplicant(String[] arrText) {
		String applicant =  Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst()
				.map(m -> m.substring(m.indexOf("O")+1, m.lastIndexOf("M")-1)
						.replace("Secretaría de", "").replace("Dirección de", "")
						.replace("Subsecretaría de", "").trim()).get();
		
		return  applicant;
	}
	
	private OrganizationDto getOrganizationDto(String applicant,OrganizationService organizationService) {
		String orgFinderRegex = "(?=.*(" + applicant + "))";
		Pattern pOrgFinderRegex = Pattern.compile(orgFinderRegex, Pattern.CASE_INSENSITIVE);
		organizationService.findAll().forEach(e -> System.out.println(e.getOrganizationName()));
		OrganizationDto findedOrgDto = organizationService.findAll().stream()
				.filter(f -> pOrgFinderRegex.matcher(f.getOrganizationName()).find()).findFirst()
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la organizacion"));
		
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

	
	private int mustReturnSupplyNumber(String[] arrText)  {
		Pattern p = Pattern.compile(supplyNumberTitleRegex);
		String number = Stream.of(arrText).filter(f -> p.matcher(f).find()).map(m -> m.substring(24, 27))
				.map(m -> m.replaceAll("[a-zA-Z\\D]", "")).collect(Collectors.joining(""));
		
		return Integer.parseInt(number);
	}

	private final String strDateV2 = "^(?=.*([0-9]{2})*([/]{1})){2}([0-9]{2,4})";

	
	private Calendar  mustReturnDate(String text){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Pattern p = Pattern.compile(strDateV2);
		String date = Stream.of(text.split(" ")).filter(f -> p.matcher(f).find()).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace("/", "-").trim();

		
		try {
		cal.setTime(sdf.parse(date));
		return cal;
		}catch(ParseException ex) {
			throw new RuntimeException("Error al parsear la fecha");
		}
	}

	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	//private final String itemMeasureUnitRegex ="(?=.*(cada)?(kilogramo)?)";

	
	private List<SupplyItemDto> mustReturnSupplyItemList(String[] arrText)  {
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
					if (i.contains(".")) {
						i = i.replace(",", "");
						i = i.substring(0, i.indexOf("."));
						supplyItemDto.setQuantity(Integer.parseInt(i));
					} else {

						char q = i.charAt(0);
						supplyItemDto.setQuantity(Integer.parseInt(Character.toString(q)));
					}
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

}
