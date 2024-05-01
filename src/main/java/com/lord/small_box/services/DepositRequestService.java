package com.lord.small_box.services;

import com.lord.small_box.dtos.DepositRequestDto;

public interface DepositRequestService {
	
	public DepositRequestDto createRequest(DepositRequestDto depositRequestDto,long organizationId);
	
	public String sendRequest(long depositRequestId,long destinationOrganization); 

}
