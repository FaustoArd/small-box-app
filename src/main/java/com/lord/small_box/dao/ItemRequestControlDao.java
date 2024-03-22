package com.lord.small_box.dao;

import java.util.List;

import com.lord.small_box.models.ItemRequestControl;

public interface ItemRequestControlDao {
	
	public ItemRequestControl saveItemRequestControl(ItemRequestControl itemRequestControl);
	
	public ItemRequestControl findItemRequestControlById(Long id);
	
	public void deleteItemRequestControlById(Long id);
	
	public List<ItemRequestControl> findAllItemRequestControls();

}
