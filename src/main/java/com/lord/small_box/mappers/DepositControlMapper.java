package com.lord.small_box.mappers;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.models.DepositControl;

@Mapper
public interface DepositControlMapper {
	
	public static DepositControlMapper INSTANCE = Mappers.getMapper(DepositControlMapper.class);
	
	
	public DepositControlDto depositControlToDto(DepositControl depositControl);

	@Mapping(target = "deposit",ignore = true)
	public DepositControl dtoToDepositControl(DepositControlDto depositControlDto);
	
	public List<DepositControlDto> depositControlsToDtos(List<DepositControl> depositControls);
	
	public List<DepositControl> dtosToDepositControls(List<DepositControlDto> depositControlDtos);
}
