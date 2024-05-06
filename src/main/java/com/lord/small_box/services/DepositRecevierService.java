package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.DepositControlReceiverDto;
import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositReceiverDto;


public interface DepositRecevierService {
	
	public List<DepositReceiverDto> findAllReceiversByOrganization(long organizationId);
	
	public boolean markAsReaded(long depositReceaverId);
	
	public long countMessages(long organizationId);
	
	public List<DepositControlReceiverDto> findAllByDepositReceiver(long depositReceiverId);
	


}
