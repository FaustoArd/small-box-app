package com.lord.small_box.dtos;

import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WorkTemplateDto {
	
	
	private Long id;
	
	private Calendar date;
	
	private String correspond;
	
	private String correspondNumber;
	
	private String producedBy;
	
	private ArrayList<String> destinations;
	
	private ArrayList<String> refs;
	
	private ArrayList<String> items;
	
	private String text;
	
	private Long organizationId;

}
