package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.models.PurchaseOrderItem;

@Mapper
public interface PurchaseOrderItemMapper {
	
	public static PurchaseOrderItemMapper INSTANCE = Mappers.getMapper(PurchaseOrderItemMapper.class);
	
	@Mapping(target="purchaseOrder.id", source="purchaseOrderId")
	public PurchaseOrderItem dtoToItem(PurchaseOrderItemDto itemDto);
	
	public List<PurchaseOrderItem> dtoToItems(List<PurchaseOrderItemDto> itemDtos);
	
	public List<PurchaseOrderItemDto> itemsToDtos(List<PurchaseOrderItem> items);

}
