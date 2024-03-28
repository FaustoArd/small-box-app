package com.lord.small_box.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.models.PurchaseOrder;

@Mapper
public interface   PurchaseOrderMapper {

	public static PurchaseOrderMapper INSTANCE = Mappers.getMapper(PurchaseOrderMapper.class);
	
	
	
	@Mapping(target="executerUnit.id", source="executerUnitOrganizationId")
	@Mapping(target="executerUnit.organizationName", source="executerUnit")
	@Mapping(target="dependency.organizationName", source="dependency")
	@Mapping(target="dependency.id", source="dependencyOrganizacionId")
	@Mapping(target="items", ignore = true)
	public  PurchaseOrder dtoToOrder(PurchaseOrderDto purchaseOrderDto);
	
	
	//@Mapping(target="executerUnit",source = "executerUnit.organizationName")
	//@Mapping(target="dependency", source="dependency.organizationName")
	@Mapping(target="executerUnitOrganizationId",source = "executerUnit.id")
	@Mapping(target="executerUnit", ignore = true)
	@Mapping(target="dependency", ignore = true)
	@Mapping(target="dependencyOrganizacionId",source = "dependency.id")
	@Mapping(target="items", ignore = true)
	public  PurchaseOrderDto orderToDto(PurchaseOrder purchaseOrder); 
		
	
		
	
}
