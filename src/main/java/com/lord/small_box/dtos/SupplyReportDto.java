package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupplyReportDto {
	
	private String supplyItemCode;
	
	private String supplyItemMeasureUnit;
	
	private String supplyItemDetail;
	
	private int supplyItemQuantity;
	
	private String depositItemCode;
	
	private String depositItemMeasureUnit;
	
	private String depositItemDetail;
	
	private int depositItemQuantity;
	
	private int depositQuantityLeft;
	
	

}
