package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.BigBagItem;

@Mapper
public interface BigBagItemMapper {

	public static final BigBagItemMapper INSTANCE = Mappers.getMapper(BigBagItemMapper.class);
	
	@Mapping(target="bigBagId",source="bigBag.id")
	@Mapping(target = "depositControlId",ignore = true)
	public BigBagItemDto itemToDto(BigBagItem bigBag);
	
	@Mapping(target = "bigBag.id", source="bigBagId")
	public BigBagItem dtoToItem(BigBagItemDto bigBagItemDto);
	
	public List<BigBagItemDto> itemstoDtos(List<BigBagItem> items);
}
