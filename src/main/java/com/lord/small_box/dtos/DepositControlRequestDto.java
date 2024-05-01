package com.lord.small_box.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositControlRequestDto {
private Long id;
	
	private String itemCode;
	
	private String itemMeasureUnit;
	
	private String itemDescription;
	
	
	private long depositRequestId;
}
