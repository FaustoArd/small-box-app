package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderDto {
	private Long id;

	private int orderNumber;

	private String jurisdiction;

	private String executerUnit;
	
	private String financingSource;

	private String dependency;
	
	private String provider;

	private String deliverTo;

	private Calendar date;

	private String exp;
	
	private BigDecimal purchaseOrderTotal;

	private List<PurchaseOrderItemDto> items;
}
