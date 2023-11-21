package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmallBoxDto {

	
	private Integer id;
	
	private Calendar date;
	
	private String ticketNumber;
	
	private String provider;
	
	private Integer inputId;
	
	private String description;
	
	private BigDecimal ticketTotal;
	
	private String inputNumber;
	
	private Integer viewOrder;
	
	private Integer containerId;
	

}
