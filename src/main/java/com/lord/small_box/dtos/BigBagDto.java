package com.lord.small_box.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigBagDto {
	
	private Long id;
	
	private String name;
	
	private int totalBigBagQuantityAvailable;
	
	private List<BigBagItemDto> items;
}
