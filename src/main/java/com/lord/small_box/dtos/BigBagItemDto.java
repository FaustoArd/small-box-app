package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigBagItemDto {
	
	private Long id;
	
	private String code;
	
	private String measureUnit;
	
	private int quantity;
	
	private BigDecimal unitCost;
	
	private BigDecimal totalCost;
	
	private Calendar expirationDate;

}
