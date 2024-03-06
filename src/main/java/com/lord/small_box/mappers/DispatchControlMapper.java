package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DispatchControlDto;
import com.lord.small_box.models.DispatchControl;

@Mapper
public interface DispatchControlMapper {

	public static final DispatchControlMapper INSTANCE = Mappers.getMapper(DispatchControlMapper.class);
	
	@Mapping(target = "organization.id", source="organizationId")
	public DispatchControl dtoToDispatch(DispatchControlDto dispatchControlDto);
	
	@Mapping(target = "organizationId", source="organization.id")
	public DispatchControlDto dispatchToDto(DispatchControl dispatchControl);
	
	public List<DispatchControlDto> dispatchsToDtos(List<DispatchControl> dispatchs);
}
