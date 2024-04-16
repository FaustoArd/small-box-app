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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.services.OrganizationService;


@Component
public class TextToSupply {
	
	private static final Logger log = LoggerFactory.getLogger(TextToSupply.class);
	
	private final List<String> measureUnits = List.of("cada", "kilogramo", "unidad");
	
	public SupplyDto textToSupply(String text,OrganizationService organizationService) {
		log.info("Text to Supply main method");
		SupplyDto supplyDto = new SupplyDto();
		String[] arrTextSplitN = text.split("\\n");
		supplyDto.setSupplyNumber(getSupplyNumber(arrTextSplitN));
		supplyDto.setDate(getDate(text));
		supplyDto.setSupplyItems(getSupplyItemList(arrTextSplitN));
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
		log.info("Text to Supply Get applicant");
		String applicant =  Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst()
				.map(m -> m.substring(m.indexOf("O")+1, m.lastIndexOf("M")-1)
						.replace("Secretaría de", "").replace("Dirección de", "")
						.replace("Subsecretaría de", "").trim()).get();
		
		return  applicant;
	}
	
	private BigDecimal getEstimatedTotal(String[] arrText) {
		log.info("Text to Supply Get Estimated total");
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

	
	private int getSupplyNumber(String[] arrText)  {
		log.info("Text to Supply Get supply number");

		Pattern p = Pattern.compile(supplyNumberTitleRegex);
		String number = Stream.of(arrText).filter(f -> p.matcher(f).find()).map(m -> m.substring(24, 28))
				.map(m -> m.replaceAll("[a-zA-Z\\D]", "")).collect(Collectors.joining(""));
		
		return Integer.parseInt(number);
	}

	private final String strDateV2 = "^(?=.*([0-9]{2})*([/]{1})){2}([0-9]{2,4})";

	
	private Calendar  getDate(String text){
		log.info("Text to Supply Get date");

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
	private final String itemQuantityRegex = "([0-9]*[,]{1})?([0-9]{1,3}[.]{1}[0-9]{2}){1}";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	//private final String itemMeasureUnitRegex ="(?=.*(cada)?(kilogramo)?)";

	Pattern pCode = Pattern.compile(itemCodeRegex);
	Pattern pProgCat = Pattern.compile(itemProgCatRegex);
	Pattern pQuantity = Pattern.compile(itemQuantityRegex);
	Pattern pUnitPrice = Pattern.compile(itemUnitPrice);
	
	private List<SupplyItemDto> getSupplyItemList(String[] arrText)  {
		log.info("Text to Supply Get supply items");
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
				if (pQuantity.matcher(i).matches()) {
					System.err.println("Quantity: " + i);
					if (i.contains(",")) {
						i = i.replace(",", "");
						
						supplyItemDto.setQuantity(new BigDecimal(i).intValue());
					} else {

						
						supplyItemDto.setQuantity(new BigDecimal(i).intValue());
					}

				}
				if(pUnitPrice.matcher(i).find()) {
					i= i.replace(".", "");
					i = i.replace(",", ".");
							
					supplyItemDto.setUnitCost(new BigDecimal(i));
				}
				if(isMeasureUnit(i)) {
					if(i.toLowerCase().contains("cada")) {
						i = i + " UNO";
					}
					supplyItemDto.setMeasureUnit(i);
				}

			});

			return supplyItemDto;
		}).toList();
		
	}
	private boolean isMeasureUnit(String i){
		return 	measureUnits.stream().filter(f -> i.toLowerCase().contains(f.toLowerCase())).findFirst().isPresent();
		}

}
