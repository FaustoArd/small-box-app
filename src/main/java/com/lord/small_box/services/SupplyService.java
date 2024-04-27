package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;

public interface SupplyService {
	
	public SupplyDto collectSupplyFromText(String text, long organizationId);

	public List<SupplyReportDto> createSupplyReport(long supplyId, long depositId);

	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId, Long depositId);

	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId);

	public List<SupplyItemDto> findSupplyItems(long supplyId);

	public int deleteSupply(long supplyId);

	public SupplyDto findsupply(long supplyId);
}
