package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmallBoxUnifierDto {
	
	private Integer id;
	
	private Calendar date;
	
	private String ticketNumber;
	
	private String provider;
	
	private String description;
	
	private String inputNumber;

	private BigDecimal ticketTotal;
	
	private String subtotalTitle;
	
	private BigDecimal subtotal;
	
	private BigDecimal total;
	
	
}
