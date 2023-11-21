package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SubTotal;

public interface SmallBoxService {
	
	public List<SmallBox> findAll();
	
	public SmallBox findById(Integer id);
	
	public SmallBox save(SmallBox smallBox);
	
	public void delete(Integer id);
	
	public List<SmallBox> findAllOrderByInputInputNumber(String inpuNumber);
	
	public List<SmallBox> findAllByContainer(Integer conainerId);
	
	public SubTotal calculateSubtotal(List<SmallBox> smallBoxes,Integer inputNumber);

}
