package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBox;

public interface SmallBoxService {
	
	public List<SmallBox> findAll();
	
	public SmallBox findById(Integer id);
	
	public SmallBox save(SmallBox smallBox);
	
	public void delete(Integer id);
	
	public List<SmallBox> findAllOrderByInputInputNumber(String inpuNumber);

}
