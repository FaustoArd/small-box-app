package com.lord.small_box.dtos;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupplyDto {

	private Long id;

	private Calendar date;

	private String jurisdiction;
	
	private int supplyNumber;

	private List<SupplyItemDto> supplyItems;
	
	private BigDecimal estimatedTotalCost;
	
	private String dependencyApplicant;
	
	private String organizationApplicant;
	
	
}
