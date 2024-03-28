package com.lord.small_box.services;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.models.PurchaseOrder;

public interface SupplyControlService {
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text);
	
	public PurchaseOrderDto findFullPurchaseOrder(Long id);
	
	public String loadPurchaseOrderToSupplyControl(Long purchaseOrderId);
	

}
