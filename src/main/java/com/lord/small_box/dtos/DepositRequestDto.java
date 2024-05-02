package com.lord.small_box.dtos;

import java.util.Calendar;
import java.util.List;

import com.lord.small_box.models.Organization;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositRequestDto {
	
	private Long id;
	
	private Calendar requestDate;
	
	
	private long organizationId;
	
	private List<DepositControlRequestDto> depositControlRequestDtos;
	
	private String requestCode;

}
