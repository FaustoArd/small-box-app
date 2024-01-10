package com.lord.small_box.mappers;

import org.springframework.stereotype.Component;

import com.lord.small_box.dtos.OrganizationResponsibleDto;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;

@Component
public class OrganizationResponsibleMapper {
	
	public OrganizationResponsibleDto toDto(OrganizationResponsible organizationResponsible) {
		if(organizationResponsible==null) {
			return null;
		}
		OrganizationResponsibleDto dto = new OrganizationResponsibleDto();
		dto.setId(organizationResponsible.getId());
		dto.setName(organizationResponsible.getName());
		dto.setLastname(organizationResponsible.getLastname());
		
		return dto;
	}
	public OrganizationResponsible toModel(OrganizationResponsibleDto organizationResponsibleDto) {
		if(organizationResponsibleDto==null) {
			return null;
		}
		OrganizationResponsible organizationResponsible = new OrganizationResponsible();
		organizationResponsible.setId(organizationResponsible.getId());
		organizationResponsible.setName(organizationResponsibleDto.getName());
		organizationResponsible.setLastname(organizationResponsible.getLastname());
		organizationResponsible.setLastname(organizationResponsible.getLastname());
		return organizationResponsible;
		}
	

}
