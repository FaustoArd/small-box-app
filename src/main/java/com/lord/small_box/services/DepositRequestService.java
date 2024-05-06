package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.dtos.OrganizationDto;


public interface DepositRequestService {
	
	public DepositRequestDto createRequest(DepositRequestDto depositRequestDto);
	
	public DepositRequestDto setDestinationOrganization(DepositRequestDto depositRequestDto);
	
	public DepositRequestDto saveItemsToRequest(DepositRequestDto depositRequestDto);
	
	public String sendRequest(long depositRequestId); 

	public List<DepositRequestDto> findAllRequestbyOrganizationByUserId(long userId);
	
	public List<DepositControlRequestDto> findAllControlRequestsByDepositRequest(long depositRequestId);
}
