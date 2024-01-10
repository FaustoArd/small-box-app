package com.lord.small_box.dtos;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrganizationDto {
	
	
	private Long id;
	
	private String organizationName;
	
	private int organizationNumber;
	
	private String responsible;
	


}
