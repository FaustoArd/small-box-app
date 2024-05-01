package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.models.DepositRequest;

@Mapper
public interface DepositRequestMapper {
	
	public static final DepositRequestMapper INSTANCE = Mappers.getMapper(DepositRequestMapper.class);
	
	@Mapping(target="organization.id",source="organizationId")
	public DepositRequest dtoToRequest(DepositRequestDto depositRequestDto);
	
	@Mapping(target="organizationId", source="organization.id")
	@Mapping(target="depositControlRequestDtos", ignore = true)
	public DepositRequestDto requestToDto(DepositRequest depositRequest);
	
	List<DepositRequestDto> requestToDtos(List<DepositRequest> requests);


}
