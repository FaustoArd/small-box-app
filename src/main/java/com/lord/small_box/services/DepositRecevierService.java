package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.DepositControlReceiverDto;
import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.dtos.RequestComparationNoteDto;
import com.lord.small_box.dtos.WorkTemplateDto;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.DepositReceiver;
import com.lord.small_box.models.WorkTemplate;


public interface DepositRecevierService {
	
	public List<DepositReceiverDto> findAllReceiversByOrganization(long organizationId);
	
	public boolean markAsReaded(long depositReceaverId);
	
	public long countMessages(long organizationId);
	
	public List<DepositControlReceiverDto> findAllByDepositReceiver(long depositReceiverId);
	
	public String deleteDepositReceiver(long depositReceiverId);
	
	public RequestComparationNoteDto createRequestComparationNote(long depositReceiverId,long depositId);
	
	public long generateRequestCorrectionMemo(long depositReceiverId,long depositId);
	
	
	

}
