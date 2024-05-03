package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositControlReceiverDto {

	private Long id;

	private String itemCode;

	private String itemMeasureUnit;

	private int itemQuantity;
	
	private String itemDescription;

	private long depositReceiverId;

}
