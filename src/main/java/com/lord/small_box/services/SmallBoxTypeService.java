package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBoxType;

public interface SmallBoxTypeService {
	
	public SmallBoxType findById(Long id);
	public SmallBoxType save(SmallBoxType smallBoxType);
	public List<SmallBoxType> findAll();
	public void delete(Long id);

}
