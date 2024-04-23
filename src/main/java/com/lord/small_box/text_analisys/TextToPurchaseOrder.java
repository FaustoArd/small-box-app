package com.lord.small_box.text_analisys;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.services.OrganizationService;

@Component
public class TextToPurchaseOrder {

	private static final Logger log = LoggerFactory.getLogger(TextToPurchaseOrder.class);

	// This method collect all PurchaseOrder elements from a pdf text and return
	// PurchaseOrder object.
	public PurchaseOrderDto textToPurchaseOrder(String text, OrganizationService organizationService) {
		log.info("Text to purchase order main method");
		String[] arrTextSplitN = text.split("\\n");

		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setDate(getDate(text));
		purchaseOrderDto.setOrderNumber(Integer.parseInt(getPurchaseOrderNumber(arrTextSplitN)));
		purchaseOrderDto.setFinancingSource(getFinancingSource(arrTextSplitN));
		purchaseOrderDto.setItems(getItems(arrTextSplitN));
		purchaseOrderDto.setPurchaseOrderTotal(getPurchaseTotal(arrTextSplitN));
		purchaseOrderDto.setExecuterUnit(getExecuterUnit(arrTextSplitN));
		purchaseOrderDto.setDependency(getDependency(arrTextSplitN));
		// System.err.println("Order Number:" + purchaseOrderDto.getOrderNumber());
		// System.err.println("Order TOTAL: " +
		// purchaseOrderDto.getPurchaseOrderTotal());
		// System.err.println("Order Date: " + purchaseOrderDto.getDate());
		// System.err.println("Purchase order: " + purchaseOrderDto.getItems());
		return purchaseOrderDto;
		/*
		 * PurchaseOrderDto purchaseOrder = PurchaseOrder.builder()
		 * .orderNumber(Integer.parseInt(getPurchaseOrderNumber(arrTextSplitN))).date(
		 * getDate(text))
		 * .items(getItems(arrTextSplitN)).purchaseOrderTotal(getPurchaseTotal(
		 * arrTextSplitN)).build();
		 */

	}

	private final String executerUnitRegex = "^(?=.*(unidad ejecutora))";

	private String getExecuterUnit(String[] arrText) {
		log.info("Text to purchase order Get Executer unit");
		Pattern pExecUnit = Pattern.compile(executerUnitRegex, Pattern.CASE_INSENSITIVE);
		String executerInut = Stream.of(arrText).filter(f -> pExecUnit.matcher(f).find())
				.map(m -> m.substring(m.indexOf(":") + 1, m.lastIndexOf(":") - 5)).findFirst().get()
				.replace("- Secretarķa", "").trim();
		return executerInut;

	}

	private final String financingSourceRegex = "(?=.*(fuente de financiamiento))";

	private String getFinancingSource(String[] arrText) {
		log.info("Text to purchase order Get Financing source");
		Pattern pFinancingSource = Pattern.compile(financingSourceRegex, Pattern.CASE_INSENSITIVE);
		String financingSourceLine = Stream.of(arrText).filter(f -> pFinancingSource.matcher(f).find()).findFirst()
				.orElse("No encontrado");
		return financingSourceLine.substring(financingSourceLine.indexOf(":") + 1, financingSourceLine.indexOf(":") + 5)
				.trim();

	}

	private final String dependencyRegex = "^(?=.*(dependencia))";

	private String getDependency(String[] arrText) {
		log.info("Text to purchase order Get Dependency");
		Pattern pDependency = Pattern.compile(dependencyRegex, Pattern.CASE_INSENSITIVE);
		String dependency = Stream.of(arrText).filter(f -> pDependency.matcher(f).find())
				.map(m -> m.substring(m.trim().indexOf(":") + 1, m.length() - 1)).findFirst().get().trim();
		return dependency;
	}

	private String getPurchaseOrderNumber(String[] arrText) {
		log.info("Text to purchase order Get purchase order number");
		return Stream.of(arrText).filter(f -> f.contains("MUNICIPIO")).findFirst().get().replaceAll("[\\D]", "")
				.strip();
	}

	private BigDecimal getPurchaseTotal(String[] arrText) {
		log.info("Text to purchase order Get purchase total");
		return new BigDecimal(Stream.of(arrText).filter(f -> f.toLowerCase().contains("total:")).findFirst().get()
				.replaceAll("[a-zA-Z]", "").replace(":", "").replace("$", "").replace(".", "").replace(",", ".")
				.strip());
	}

	// REGEX to find items data.
	private final String itemCodeRegex = "^(?=.*([0-9].){3}([0-9]){5}(.)([0-9]){4})";
	private final String itemQuantityRegex = "^(?=.*([0-9])*(,)([0-9]){3})";
	private final String itemProgCatRegex = "^(([0-9]){2}(.)([0-9]){2}(.)([0-9]){2})";
	private final String itemUnitPrice = "^(?=.*([0-9].)*(,)([0-9]){5})";
	private final Pattern pItemCode = Pattern.compile(itemCodeRegex);
	private final Pattern pItemQuantity = Pattern.compile(itemQuantityRegex);
	private final Pattern pProgCat = Pattern.compile(itemProgCatRegex);
	private final Pattern pUnitPrice = Pattern.compile(itemUnitPrice);

	// This method find all the purchase order items.
	private List<PurchaseOrderItemDto> getItems(String[] arrText) {
		log.info("Text to purchase order Get Items");
		// This list contains all lines that match the item code REGEX
		List<String> itemsText = Stream.of(arrText).filter(f -> pItemCode.matcher(f).find())
				.collect(Collectors.toList());

		// Iterate the list and save each item element in a new PurchaseOrderItem
		// object.
		return itemsText.stream().map(item -> {

			PurchaseOrderItemDto purchaseOrderItem = new PurchaseOrderItemDto();
			String[] arrItems = item.split(" ");
			purchaseOrderItem
					.setCode(Stream.of(arrItems).filter(f -> pItemCode.matcher(f).find()).findFirst().get().strip());

			purchaseOrderItem
					.setQuantity(Integer.parseInt(Stream.of(arrItems).filter(f -> pItemQuantity.matcher(f).find())
							.map(m -> m.substring(0, m.indexOf(","))).findFirst().get()));

			String measureUnitResult = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).findFirst().get();
			if (measureUnitResult.equalsIgnoreCase("cada")) {
				measureUnitResult = measureUnitResult + "-UNO";
			}
			purchaseOrderItem.setMeasureUnit(measureUnitResult);

			purchaseOrderItem.setProgramaticCat(
					Stream.of(arrItems).filter(f -> pProgCat.matcher(f).find()).findFirst().get().strip());

			String itemDetail = Stream.of(arrItems).filter(f -> f.matches("([a-zA-Z]*)")).skip(1)
					.map(m -> m.replaceAll("[0-9\\W]", "")).collect(Collectors.joining("-"));
			if (itemDetail.startsWith("UNO-")) {
				itemDetail = itemDetail.substring(itemDetail.indexOf("-") + 1, itemDetail.length() - 1);
			}
			purchaseOrderItem.setItemDetail(itemDetail);
			purchaseOrderItem.setUnitCost(new BigDecimal(Stream.of(arrItems).filter(f -> pUnitPrice.matcher(f).find())
					.map(m -> m.replace(".", "").replace(",", ".")).findFirst().get()));

			purchaseOrderItem.setTotalEstimatedCost(new BigDecimal(
					item.substring(item.lastIndexOf("$") + 1).replace(".", "").replace(",", ".").strip()));
			return purchaseOrderItem;

		}).toList();
	}

	private final String strDateV2 = "^(?=.*(?=.*[0-9]{2})*(?=.*[/]{1})){2}(?=.*[0-9]{2,4})";

	private Calendar getDate(String text) {
		log.info("Text to purchase order Get date");
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

}
