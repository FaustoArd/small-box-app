package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderItemCandidateDto {

	
	private long orderItemId;
	
	private long excelItemDtoId;
	
	private String code;
	
	private String programaticCategory;
	
	private int quantity;
	
	private String measureUnit;
	
	private String itemDetail;

}
