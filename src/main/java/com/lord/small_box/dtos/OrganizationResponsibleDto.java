package com.lord.small_box.dtos;

import com.lord.small_box.models.Organization;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationResponsibleDto {
	
	private Long id;
	

	private String name;
	

	private String lastname;
	

	private Organization organization;

}
