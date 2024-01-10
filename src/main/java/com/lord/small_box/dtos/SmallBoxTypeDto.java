package com.lord.small_box.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmallBoxTypeDto {
	
	private Long id;
	
	private String smallBoxType;
	
	private int maxRotation;
	
	private BigDecimal maxAmount;
}
