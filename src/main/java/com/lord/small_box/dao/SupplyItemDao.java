package com.lord.small_box.dao;

import java.util.List;

import com.lord.small_box.models.SupplyItem;

public interface SupplyItemDao {

	public SupplyItem findSupplyItemById(Long id);
	public SupplyItem saveSupplyItem(SupplyItem supplyItem);
	public void deleteSupplyItemById(Long id);
	public List<SupplyItem> findallSupplyItems();
}
