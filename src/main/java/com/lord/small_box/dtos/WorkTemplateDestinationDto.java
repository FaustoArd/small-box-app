package com.lord.small_box.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkTemplateDestinationDto {

	private Long id;
	
	private String destination;
	
	public WorkTemplateDestinationDto() {
		super();
	}
	
	public WorkTemplateDestinationDto(String destination) {
		this.destination = destination;
	}
}
