package com.lord.small_box.dao;

import java.util.List;

import com.lord.small_box.models.PurchaseOrderItem;

public interface PurchaseOrderItemDao {
	
	public PurchaseOrderItem findItembyId(Long id);
	
	public PurchaseOrderItem saveItem(PurchaseOrderItem item);
	
	public void deleteItemById(Long id);
	
	public List<PurchaseOrderItem> findAllItems();

}
