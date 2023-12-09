package com.lord.small_box.models;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.SmallBoxTypeDto;

@Mapper
public interface SmallBoxTypeMapper {

	public static SmallBoxTypeMapper INSTANCE = Mappers.getMapper(SmallBoxTypeMapper.class);
	
	public SmallBoxType toSmallBoxType(SmallBoxTypeDto smallBoxTypeDto);
	
	public SmallBoxTypeDto toSmallBoxTypeDto(SmallBoxType smallBoxType);
	
	public List<SmallBoxTypeDto> toSmallBoxesDto(List<SmallBoxType> smallBoxesType);
	
}
