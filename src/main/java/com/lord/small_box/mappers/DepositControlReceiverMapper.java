package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositControlReceiverDto;
import com.lord.small_box.models.DepositControlReceiver;

@Mapper
public interface DepositControlReceiverMapper {
	
	public static final DepositControlReceiverMapper INSTANCE = Mappers.getMapper(DepositControlReceiverMapper.class);
	
	@Mapping(target = "depositReceiver.id",source = "depositReceiverId")
	public DepositControlReceiver dtoToReceiver(DepositControlReceiverDto depositControlRequestReceiverDto);
	
	@Mapping(target = "depositReceiverId",source = "depositReceiver.id")
	public DepositControlReceiverDto receiverToDto(DepositControlReceiver depositControlRequestReceiver);
	
	public List<DepositControlReceiverDto> receiversToDtos(List<DepositControlReceiver> receivers);

}
