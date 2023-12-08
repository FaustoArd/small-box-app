package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBoxUnifier;



public interface SmallBoxUnifierService {
	
	public List<SmallBoxUnifier> findAllSmallBoxUnifiers();
	
	public SmallBoxUnifier findSmallBoxUnifierbyId(Integer id);
	
	public SmallBoxUnifier saveSmallBoxUnifier(SmallBoxUnifier smallBoxUnifier);
	
	public void deleteSmallBoxUnifierById(Integer id);
	
	public List<SmallBoxUnifier> findByContainerId(Integer containerId);


}
