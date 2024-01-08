package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.OrganizationResponsible;

@Mapper
public abstract class ContainerMapper {

	public static ContainerMapper INSTANCE = Mappers.getMapper(ContainerMapper.class);
	
	@Mapping(target="organization.id", source="organization")
	@Mapping(target="responsible", ignore = true)
	public abstract Container toContainer(ContainerDto containerDto);
	
	@Mapping(target="responsible", source = ".", qualifiedByName = "toFullName")
	@Mapping(target="organization", source="organization.organizationName")
	public abstract ContainerDto toContainerDto(Container container);
	
	@Mapping(target="responsible", source = "responsible.name")
	@Mapping(target="organization", source="organization.organizationName")
	public abstract List<ContainerDto> toContainersDto(List<Container> containers);
	
	@Named("toFullName")
	String translateToFullName(Container container) {
		return container.getResponsible().getName() + " " + container.getResponsible().getLastname();
	}
}
