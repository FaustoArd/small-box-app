package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;

public interface ContainerService {

	public List<Container> findAll();
	
	public Container save(Container container);
	
	public Container findById(Integer id);
	
	public void deleteById(Integer id);
	
	public List<Container> findAllByOrganizations(List<Organization> organizations);
}
