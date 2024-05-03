package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.models.DepositReceiver;

@Mapper
public interface DepositRecieverMapper {

	public static final DepositRecieverMapper INSTANCE = Mappers.getMapper(DepositRecieverMapper.class);
	
	@Mapping(target = "organization.id",source="organizationId")
	@Mapping(target = "fromOrganization",ignore = true)
	public DepositReceiver dtoToReceiver(DepositReceiverDto depositReceiverDto);
	
	@Mapping(target = "organizationId",source="organization.id")
	@Mapping(target = "depositControlReceivers", ignore = true)
	@Mapping(target="fromOrganizationName",source = "fromOrganization.organizationName")
	public DepositReceiverDto receiverToDto(DepositReceiver depositReceiver);
	
	public List<DepositReceiverDto> receiversToDtos(List<DepositReceiver> receivers); 
}