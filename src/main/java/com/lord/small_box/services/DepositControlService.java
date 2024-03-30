package com.lord.small_box.services;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.PurchaseOrder;

public interface DepositControlService {
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text);
	
	public PurchaseOrderDto findFullPurchaseOrder(Long id);
	
	public String loadPurchaseOrderToDepositControl(Long purchaseOrderId);
	
	public SupplyDto loadSupply(String text);
	
	public SupplyReportDto checkDeposit(SupplyDto supplyDto);

}
