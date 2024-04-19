package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositControlDto {
    private Long id;
    
	private String place;
	
	private String itemName;
	
	private String itemCode;
	
	private int quantity;
	
	private Calendar expirationDate;
	
	private String provider;
	
	private String measureUnit;
	
	private BigDecimal itemUnitPrice;
	
	private BigDecimal itemTotalPrice;
}
