package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SubTotal;

public interface SmallBoxService {
	
	public List<SmallBox> findAll();
	
	public SmallBox findById(Integer id);
	
	public SmallBox save(SmallBox smallBox,Integer containerId);
	
	public void delete(Integer id);
	
	public List<SmallBox> findAllOrderByInputInputNumber(String inpuNumber);
	
	public List<SmallBox> findAllByContainerId(Integer containerId);
	
	public List<SmallBox> findAllByContainerIdAndInputInputNumber(Integer containerId, String inputNumber);
	
	public SubTotal calculateSubtotal(Integer conatinerid,String inputNumber);
	
	public List<SmallBox> insertSubtotalInColumn(Integer containerId);
	
	public List<SmallBox> setViewOrderByInput(Integer containerId,String inputNumber);
}
