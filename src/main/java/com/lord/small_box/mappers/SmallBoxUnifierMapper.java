package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.SmallBoxUnifierDto;
import com.lord.small_box.models.SmallBoxUnifier;

@Mapper
public interface SmallBoxUnifierMapper {

	
	public static SmallBoxUnifierMapper INSTANCE = Mappers.getMapper(SmallBoxUnifierMapper.class);
	
	@Mapping(target="container", ignore=true)
	public SmallBoxUnifier toSmallBoxBuilder(SmallBoxUnifierDto smallBoxBuilderDto);
	
	public SmallBoxUnifierDto toSmallBoxBuilderDto(SmallBoxUnifier smallBoxBuilder);
	
	public List<SmallBoxUnifierDto> toSmallBoxBuildersDto(List<SmallBoxUnifier> smallBoxBuilders);
}
