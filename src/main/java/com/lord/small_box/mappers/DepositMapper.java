package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.models.Deposit;

@Mapper
public interface DepositMapper {
	public static DepositMapper INSTANCE = Mappers.getMapper(DepositMapper.class);
	
	@Mapping(target = "organization.id", source="organizationId")
	public Deposit dtoToDeposit(DepositDto depositDto);
	
	@Mapping(target="organizationId", source="organization.id")
	@Mapping(target="organizationName", source="organization.organizationName")
	public DepositDto depositToDto(Deposit deposit);
	
	public List<DepositDto> depositToDtos(List<Deposit> deposits);

}
