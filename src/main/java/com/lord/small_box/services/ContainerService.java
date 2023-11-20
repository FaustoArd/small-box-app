package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.Container;

public interface ContainerService {

	public List<Container> findAll();
	
	public Container save(Container container);
	
	public Container findById(Long id);
	
	public void deleteById(Long id);
}
