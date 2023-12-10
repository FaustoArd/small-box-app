package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainerDto {

	private Integer id;
	
	private BigDecimal total;
	
	private String title;
	
	private String dependency;
	
	private String responsible;
	
	private Calendar smallBoxDate;
	
	private boolean smallBoxCreated;
}
