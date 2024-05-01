package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.models.DepositControlRequest;

@Mapper
public interface DepositControlRequestMapper {

	public static final DepositControlRequestMapper INSTANCE = Mappers.getMapper(DepositControlRequestMapper.class);
	
	@Mapping(target = "depositRequest.id",source = "depositRequestId")
	public DepositControlRequest dtoToRequest(DepositControlRequestDto depositControlRequestDto);
	
	@Mapping(target = "depositRequestId",source = "depositRequest.id")
	public DepositControlRequestDto requestToDto(DepositControlRequest depositControlRequest);
	
	public List<DepositControlRequestDto> requestsToDtos(List<DepositControlRequest> request);
}
