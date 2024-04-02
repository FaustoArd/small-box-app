package com.lord.small_box.dao;

import java.util.List;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;

public interface PurchaseOrderDao {
	
	public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);
	
	public PurchaseOrder findPurchaseOrderById(Long id);
	
	public void deletePurchaseOrderById(Long id);
	
	
	
	public List<PurchaseOrder> findAllByOrganization(Organization organization);

	
}
