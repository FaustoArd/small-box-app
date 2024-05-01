package com.lord.small_box.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositReceiverDto {

	private Long id;

	private String receptionDate;

	private long organizationId;

	List<DepositControlReceiverDto> depositControlReceivers;

}
