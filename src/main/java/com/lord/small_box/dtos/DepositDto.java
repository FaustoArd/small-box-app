package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositDto {

	private Long id;
	private String name;
	private String streetName;
	private String houseNumber;
	private String organizationName;
	private long organizationId;
}
