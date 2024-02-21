package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.LocationContractDto;
import com.lord.small_box.models.LocationContract;

@Mapper
public interface LocationContractMapper {

	public static LocationContractMapper INSTANCE = Mappers.getMapper(LocationContractMapper.class);
	
	public LocationContract dtoToLocationContract(LocationContractDto locationContractDto);
	
	public LocationContractDto locationContractToDto(LocationContract locationContract);
	
	public List<LocationContractDto> locationContractsToDtos(List<LocationContract> contracts);
}
