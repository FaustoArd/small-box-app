package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateDependencyItemReportDto {
	
	private String depositMode;
	
	private String itemCode;
	
	private int itemQuantity;
	
	public UpdateDependencyItemReportDto(String depositMode,String itemCode,int itemQuantity) {
		this.depositMode = depositMode;
		this.itemCode = itemCode;
		this.itemQuantity = itemQuantity;
	}

}
