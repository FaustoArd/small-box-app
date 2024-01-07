package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBoxUnifier;



public interface SmallBoxUnifierService {
	
	public List<SmallBoxUnifier> findAllSmallBoxUnifiers();
	
	public SmallBoxUnifier findSmallBoxUnifierbyId(Long id);
	
	public SmallBoxUnifier saveSmallBoxUnifier(SmallBoxUnifier smallBoxUnifier);
	
	public void deleteSmallBoxUnifierById(Long id);
	
	public List<SmallBoxUnifier> findByContainerId(Long containerId);
	
	public void deleteAllByContainerId(Long containerId);
	
	


}
