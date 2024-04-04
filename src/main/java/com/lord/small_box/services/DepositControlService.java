package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.DepositControlDto;
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
	
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId);
	
	public SupplyDto collectSupplyFromText(String text, long organizationId);
	
	public List<SupplyReportDto> createSupplyReport(long supplyId);
	
	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId);
	
	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId);
	
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId);
	
	public List<SupplyItemDto> findSupplyItems(long supplyId);
	
	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId);
	
	public List<DepositControlDto> findDepositControlsByOrganization(long organizationId);

}
