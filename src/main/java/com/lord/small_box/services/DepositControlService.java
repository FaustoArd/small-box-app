package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyCorrectionNote;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.SupplyItem;

public interface DepositControlService {
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text, long organizationId);
	
	public PurchaseOrderDto findFullPurchaseOrder(Long id);
	
	public List<String> loadPurchaseOrderToDepositControl(Long purchaseOrderId);
	
	public SupplyDto collectSupplyFromText(String text, long organizationId);
	
	public List<SupplyReportDto> createSupplyReport(long supplyId);
	
	public SupplyCorrectionNote createSupplyCorrectionNote(long supplyId);
	
	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId);
	
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId);

}
