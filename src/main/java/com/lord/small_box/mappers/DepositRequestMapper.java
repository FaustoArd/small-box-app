package com.lord.small_box.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.models.DepositRequest;
import com.lord.small_box.models.Organization;

@Mapper
public interface DepositRequestMapper {
	
	public static final DepositRequestMapper INSTANCE = Mappers.getMapper(DepositRequestMapper.class);
	
	
	@Mapping(target="destinationOrganization.id", source = "destinationOrganizationId")
	@Mapping(target="mainOrganization.id",source="mainOrganizationId")
	public DepositRequest dtoToRequest(DepositRequestDto depositRequestDto);
	
	@Mapping(target="mainOrganizationId", source="mainOrganization.id")
	@Mapping(target="destinationOrganizationId",source = "destinationOrganization.id")
	@Mapping(target="destinationOrganizationName",source = "destinationOrganization.organizationName")
	@Mapping(target="depositControlRequestDtos", ignore = true)
	public DepositRequestDto requestToDto(DepositRequest depositRequest);
	
	List<DepositRequestDto> requestToDtos(List<DepositRequest> requests);

}
