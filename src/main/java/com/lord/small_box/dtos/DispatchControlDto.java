package com.lord.small_box.dtos;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispatchControlDto {
	
	private Long id;
	
	private Calendar date;
	
	private String type;
	
	private String docNumber;
	
	private String volumeNumber;
	
	private String description;
	
	private String toDependency;
	
	public Long organizationId;


}