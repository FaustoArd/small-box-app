package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyCorrectionNote;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.SupplyItem;

public interface DepositControlService {
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text);
	
	public PurchaseOrderDto findFullPurchaseOrder(Long id);
	
	public List<String> loadPurchaseOrderToDepositControl(Long purchaseOrderId);
	
	public SupplyDto loadSupplyFromText(String text);
	
	public List<SupplyReportDto> createSupplyReport(long supplyId);
	
	public SupplyCorrectionNote createSupplyCorrectionNote(long supplyId);

}
