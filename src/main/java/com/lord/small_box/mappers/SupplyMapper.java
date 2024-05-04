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
	
	@Mapping(target = "mainOrganization", ignore = true)
	@Mapping(target="applicantOrganization",ignore = true)
	public Supply dtoToSupply(SupplyDto supplyDto);
	
	
	@Mapping(target = "supplyItems", ignore = true)
	@Mapping(target="applicantOrganization",source="applicantOrganization.organizationName")
	public SupplyDto supplyToDto(Supply supply);
	
	public List<Supply> dtosToSupplies(List<SupplyDto> suppliesDtos);
	
	public List<SupplyDto> suppliesToDtos(List<Supply> supplies);
	
}
