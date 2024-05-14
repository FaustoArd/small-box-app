package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.DepositItemComparatorDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.models.SupplyItem;

public interface DepositControlService {

	

	

	public List<DepositControlDto> findDepositControlsByDeposit(long depositId);

	public String createDeposit(DepositDto depositDto);

	public List<DepositDto> findAllDepositsByOrganization(long organizationId);

	public DepositResponseDto setCurrentDeposit(long userId, long organizationId, long depositId);

	public DepositResponseDto getCurrentDepositId(long userId, long organizationId);

	public BigBagDto createBigBag(BigBagDto bigBagDto, long organizationId);

	public int getTotalBigBagQuantityAvailable(long bigBagId, long depositId);

	public List<BigBagDto> findAllBigBagsByOrg(long organizationId);

	public BigBagItemDto updateBigBagItemQuantity(long bigBagItemId, int quantity);

	public List<BigBagItemDto> findAllBigBagItems(long bigBagId);

	public List<DepositItemComparatorDto> getExcelToPuchaseOrderComparator(List<ExcelItemDto> excelItems,
			long organizationId);

	public List<DepositControlDto> saveExcelItemsToDepositControls(long organizationId, long depositId,
			List<ExcelItemDto> excelItemDtos);
	
	public String deleteDepositControlById(long depositControlId);
	
	public DepositControlDto findDepositControlById(long depositControlId);
	
	public DepositControlDto updateDepositControl(DepositControlDto depositControlDto,long depositId);

}
