package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainerDto {

	private Long id;
	
	private BigDecimal total;
	
	private String smallBoxType;
	
	private String organization;
	
	private String responsible;
	
	private Calendar smallBoxDate;
	
	private boolean smallBoxCreated;
	
	private String totalWrite;
}
