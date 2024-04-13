package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositResponseDto {
	private Long id;
	private String name;
	
	public DepositResponseDto(Long id,String name) {
		this.id = id;
		this.name=name;
	}
}
