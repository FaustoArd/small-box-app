package com.lord.small_box.dtos;

import java.util.List;

import com.lord.small_box.models.Organization;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDto {
	
	private Long id;
	
	private String name;
	
	private String lastname;
	
	private String username;
	
	private String email;
	
	private List<Organization> organizations;
	
	private long mainOrganizationId;

}
