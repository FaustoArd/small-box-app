package com.lord.small_box.services;

import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.models.PurchaseOrder;

public interface SupplyControlService {
	
	public PurchaseOrder collectPurchaseOrderFromText(String text);
	
	public PurchaseOrder findFullPurchaseOrder(Long id);
	
	
	

}
