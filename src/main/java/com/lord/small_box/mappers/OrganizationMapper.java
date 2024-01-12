package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;

@Mapper
public abstract class OrganizationMapper {
	
	public static OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);
	
	
	@Mapping(target="responsible.id", source="responsibleId")
	@Mapping(target="containers", ignore = true)
	public abstract Organization toOrganization(OrganizationDto organizationDto);
	
	@Mapping(target="responsible", source = ".", qualifiedByName = "toFullName")
	public abstract OrganizationDto toOrganizationDto(Organization organization);
	
	
	public abstract List<OrganizationDto> toOrganizationsDto(List<Organization> organizations);
	
	@Named("toFullName")
	String translateToFullName(Organization organization) {
		return organization.getResponsible().getName() + " " + organization.getResponsible().getLastname();
	}

}
