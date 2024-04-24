package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelItemDto {

	private long excelItemId;
	
	private long purchaseOrderId;
	
	private String itemDescription;
	
	private String itemMeasureUnit;
	
	private int quantity;
	
}
