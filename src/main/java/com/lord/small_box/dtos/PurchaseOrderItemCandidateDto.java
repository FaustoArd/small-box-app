package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderItemCandidateDto {

	
	private long orderId;
	
	private int excelItemDtoId;
	
	private String code;
	
	private String measureUnit;
	
	private String itemDetail;

}
