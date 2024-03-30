package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.models.Supply;

@Mapper
public interface SupplyMapper {

	public static SupplyMapper INSTANCE = Mappers.getMapper(SupplyMapper.class);
	
	@Mapping(target = "dependencyApplicant.organizationName", source="dependencyApplicant")
	@Mapping(target="dependencyApplicant.id", source="dependencyApplicantOrganizationId")
	public Supply dtoToSupply(SupplyDto supplyDto);
	
	
	@Mapping(target = "supplyItems", ignore = true)
	@Mapping(target = "dependencyApplicant", source="dependencyApplicant.organizationName")
	@Mapping(target="dependencyApplicantOrganizationId", source="dependencyApplicant.id")
	public SupplyDto supplyToDto(Supply supply);
	
	public List<Supply> dtosToSupplies(List<SupplyDto> suppliesDtos);
	
	public List<SupplyDto> suppliesToDtos(List<Supply> supplies);
	
}
