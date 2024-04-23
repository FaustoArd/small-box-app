package com.lord.small_box.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositItemComparatorDto {

	
	private ExcelItemDto excelItemDto;
	
	private List<PurchaseOrderItemCandidateDto> purchaseOrderItemCandidateDtos;
}
