package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ControlRequestComparationNoteDto {

	private String requestItemCode;

	private String requestItemMeasureUnit;

	private String requestItemDescription;

	private int requestItemQuantity;

	private String controlItemCode;

	private String controlItemMeasureUnit;

	private String controlItemDescription;

	private int controlItemQuantity;
	
	private int controlQuantityLeft;
}
