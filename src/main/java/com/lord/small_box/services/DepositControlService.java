package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.models.SupplyItem;

public interface DepositControlService {
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text, long organizationId);
	
	public PurchaseOrderDto findFullPurchaseOrder(Long id);
	
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId,Long depositId);
	
	public SupplyDto collectSupplyFromText(String text, long organizationId);
	
	public List<SupplyReportDto> createSupplyReport(long supplyId,long depositId);
	
	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId,Long  depositId);
	
	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId);
	
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId);
	
	public List<SupplyItemDto> findSupplyItems(long supplyId);
	
	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId);
	
	public List<DepositControlDto> findDepositControlsByDeposit(long depositId);
	
	public String createDeposit(DepositDto depositDto);
	
	public List<DepositDto> findAllDepositsbyOrganization(long organizationId);
	
	public DepositResponseDto setCurrentDeposit(long userId,long organizationId,long depositId);
	
	public DepositResponseDto getCurrentDepositId(long userId,long organizationId);
	
	//public DepositResponseDto resetCurrentUserSelectedDeposit(long userId,long currentOrgId);

}
