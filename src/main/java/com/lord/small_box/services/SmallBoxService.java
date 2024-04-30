package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.models.Subtotal;

public interface SmallBoxService {
	
	public List<SmallBox> findAll();
	
	public SmallBox findById(Long id);
	
	public SmallBox save(SmallBox smallBox,Long containerId);
	
	public SmallBox update(SmallBox smallBox);
	
	public void delete(Long id);
	
	public List<SmallBox> findAllOrderByInputInputNumber(String inpuNumber);
	
	public List<SmallBox> findAllByContainerId(Long containerId);
	
	public List<SmallBox> findAllByContainerIdAndInputInputNumber(Long containerId, String inputNumber);
	
	public Subtotal calculateSubtotal(Long containerid,String inputNumber);
	
	public List<SmallBoxUnifier> completeSmallBox(Long containerId);
	
	public void addAllTicketTotals(Long containerId);
	
	public List<SmallBox> findAllByContainerIdOrderByInputInputNumber(Long containerId);
	
	public String checkMaxAmount(Long containerId);
	
	

}
