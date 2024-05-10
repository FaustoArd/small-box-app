package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseOrderToDepositReportDto {
	
	private String depositItemCode;
	private String depositItemDescription;
	private int depositItemQuantity;
	private String depositItemMeasureUnit;
	private String depositItemStatus;
	
	public PurchaseOrderToDepositReportDto(String depositItemCode,String depositItemDescription,int depositItemQuantity,String depositItemMeasureUnit,String depositItemStatus) {
		this.depositItemCode = depositItemCode;
		this.depositItemDescription = depositItemDescription;
		this.depositItemQuantity = depositItemQuantity;
		this.depositItemMeasureUnit = depositItemMeasureUnit;
		this.depositItemStatus = depositItemStatus;
	}
	

}
