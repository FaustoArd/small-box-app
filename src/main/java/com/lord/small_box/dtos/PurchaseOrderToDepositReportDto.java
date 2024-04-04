package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseOrderToDepositReportDto {
	
	private String depositItemName;
	private int depositItemQuantity;
	private String depositItemMeasureUnit;
	private String depositItemStatus;
	
	public PurchaseOrderToDepositReportDto(String depositItemName,int depositItemQuantity,String depositItemMeasureUnit,String depositItemStatus) {
		this.depositItemName = depositItemName;
		this.depositItemQuantity = depositItemQuantity;
		this.depositItemMeasureUnit = depositItemMeasureUnit;
		this.depositItemStatus = depositItemStatus;
	}
	

}
