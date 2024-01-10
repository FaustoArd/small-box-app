package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.models.Organization;

@Mapper
public interface OrganizationMapper {
	
	public static OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);
	
	
	@Mapping(target="responsible.name", source="responsible")
	public Organization toOrganization(OrganizationDto organizationDto);
	
	@Mapping(target="responsible", source="responsible.name")
	public OrganizationDto toOrganizationDto(Organization organization);
	
	public List<OrganizationDto> toOrganizationsDto(List<Organization> organizations);

}
