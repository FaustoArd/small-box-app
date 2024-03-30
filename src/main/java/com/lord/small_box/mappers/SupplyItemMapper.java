package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.models.SupplyItem;

@Mapper
public interface SupplyItemMapper {

	public static SupplyItemMapper INSTANCE = Mappers.getMapper(SupplyItemMapper.class);
	
	public SupplyItem dtoToItem(SupplyItemDto supplyItemDto);
	
	public SupplyItemDto itemToDto(SupplyItem supplyItem);
	
	List<SupplyItem> dtoToItems(List<SupplyItemDto> suppliesDto);
	
	List<SupplyItemDto> itemToDtos(List<SupplyItem> items);
}
