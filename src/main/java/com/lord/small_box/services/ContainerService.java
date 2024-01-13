package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;

public interface ContainerService {

	public List<Container> findAll();
	
	public Container save(Container container);
	
	public Container findById(Long id);
	
	public void deleteById(Long id);
	
	public List<Container> findAllByOrganizations(List<Organization> organizations);
	
	public List<ContainerDto> findAllbyOrganizationsByUser(Long userId);
	
	public void setContainerTotalWrite(Long containerId,String totalWrite);
	
	public Container update(Container container);
}
