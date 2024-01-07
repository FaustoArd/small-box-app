package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lord.small_box.models.SubTotal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmallBoxDto {

	
	private Long id;
	
	private Calendar date;
	
	private String ticketNumber;
	
	private String provider;
	
	private Long inputId;
	
	private String description;
	
	private BigDecimal ticketTotal;
	
	private String inputNumber;
	
	private BigDecimal subtotal;
	
	private Long containerId;
	

}
