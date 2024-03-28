package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;

import com.lord.small_box.models.PurchaseOrder;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PurchaseOrderItemDto {
private Long id;
	
	private String code;
	
	private String programaticCat;
	
	private int quantity;
	
	private String measureUnit;
	
	private String itemDetail;
	
	private BigDecimal unitCost;
	
	private BigDecimal  estimatedCost;
	
	private BigDecimal totalEstimatedCost;
	
	private Calendar expirationDate;
	
	
}
