package com.lord.small_box.dtos;

import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestComparationNoteDto {

	private Calendar requestDate;
	
	private String fromOrganization;
	
	private String mainOrganization;
	
	private String requestCode;
	
	private String depositName;
	
	private List<ControlRequestComparationNoteDto> items;
}
