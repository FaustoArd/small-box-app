package com.lord.small_box.dao;

import java.util.List;

import com.lord.small_box.models.PurchaseOrderItem;

public interface PurchaseOrderItemDao {
	
	public PurchaseOrderItem findItembyId(Long id);
	
	public PurchaseOrderItem saveItem(PurchaseOrderItem item);
	
	public List<PurchaseOrderItem> saveAll(List<PurchaseOrderItem> items);
	
	public void deleteItemById(Long id);
	
	public List<PurchaseOrderItem> findAllItems();
	public List<PurchaseOrderItem> findAllbyId(List<Long>ids);
}
