package com.lord.small_box.dtos;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrganizationDto {

	private Long id;

	private String organizationName;

	private int organizationNumber;

	private String responsible;
	
	private Long responsibleId;

	private int currentRotation;

	private int maxRotation;

	private BigDecimal maxAmount;

}
