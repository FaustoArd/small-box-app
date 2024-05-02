package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplyItemRequestDto {
    private Long itemId;
	
	private String code;
	
	private int quantity;
	
	private String measureUnit;
	
	private String itemDetail;
	
	
}
