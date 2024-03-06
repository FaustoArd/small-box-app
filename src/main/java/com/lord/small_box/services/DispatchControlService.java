package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.DispatchControl;

public interface DispatchControlService {

	
	public DispatchControl save(DispatchControl dispatchControl);
	
	public List<DispatchControl> findAll();
	
	public DispatchControl findById(Long id);
	
	public String deleteById(Long id);
}
