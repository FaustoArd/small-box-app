package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.models.Container;

@Mapper
public interface ContainerMapper {

	public static ContainerMapper INSTANCE = Mappers.getMapper(ContainerMapper.class);
	
	@Mapping(target="organization.id", source="organization")
	public Container toContainer(ContainerDto containerDto);
	
	@Mapping(target="organization", source="organization.organizationName")
	public ContainerDto toContainerDto(Container container);
	
	@Mapping(target="organization", source="organization.organizationName")
	public List<ContainerDto> toContainersDto(List<Container> containers);
}
