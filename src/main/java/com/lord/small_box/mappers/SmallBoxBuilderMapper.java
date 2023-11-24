package com.lord.small_box.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.SmallBoxBuilderDto;
import com.lord.small_box.models.SmallBoxBuilder;

@Mapper
public interface SmallBoxBuilderMapper {

	
	public static SmallBoxBuilderMapper INSTANCE = Mappers.getMapper(SmallBoxBuilderMapper.class);
	
	@Mapping(target="container", ignore=true)
	public SmallBoxBuilder toSmallBoxBuilder(SmallBoxBuilderDto smallBoxBuilderDto);
	
	public SmallBoxBuilderDto toSmallBoxBuilderDto(SmallBoxBuilder smallBoxBuilder);
}
