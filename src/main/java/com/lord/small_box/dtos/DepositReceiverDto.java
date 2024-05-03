package com.lord.small_box.dtos;

import java.util.Calendar;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositReceiverDto {

	private Long id;

	private Calendar receptionDate;

	private long organizationId;

	List<DepositControlReceiverDto> depositControlReceivers;
	
	private String depositRequestCode;
	
	private boolean readed;

}
