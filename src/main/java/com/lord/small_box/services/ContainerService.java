package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.Container;

public interface ContainerService {

	public List<Container> findAll();
	
	public Container save(Container container);
	
	public Container findById(Integer id);
	
	public void deleteById(Integer id);
}
