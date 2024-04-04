package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.models.PurchaseOrder;

@Mapper
public interface   PurchaseOrderMapper {

	public static PurchaseOrderMapper INSTANCE = Mappers.getMapper(PurchaseOrderMapper.class);
	
	@Mapping(target = "organization", ignore = true)
	public  PurchaseOrder dtoToOrder(PurchaseOrderDto purchaseOrderDto);
	
	@Mapping(target="items", ignore = true)
	public  PurchaseOrderDto orderToDto(PurchaseOrder purchaseOrder); 
		
	public List<PurchaseOrderDto> ordersToDtos(List<PurchaseOrder> orders);
	
}
