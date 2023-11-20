package com.lord.small_box.dtos;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContainerDto {

	private Integer id;
	
	private Double total;
	
	private Calendar smallBoxDate;
}
