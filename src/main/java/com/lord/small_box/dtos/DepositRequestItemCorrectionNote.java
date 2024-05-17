package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositRequestItemCorrectionNote {
	
	private String itemCode;
	private String itemMeasureUnit;
	private String itemDescription;
	private int itemOldQuantity;
	private int itemNewQuantity;
}
