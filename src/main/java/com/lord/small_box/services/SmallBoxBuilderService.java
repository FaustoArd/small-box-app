package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.SmallBox.SmallBoxBuilder;

public interface SmallBoxBuilderService {
	
	public List<SmallBoxBuilder> findAllSmallBoxBuilders();
	
	public SmallBoxBuilder findSmallBoxBuilderbyId(Integer id);
	
	public SmallBoxBuilder saveSmallBoxBuilder(SmallBoxBuilder smallBoxBuilder);
	
	public void deleteSmallBoxBuilderById(Integer id);


}
