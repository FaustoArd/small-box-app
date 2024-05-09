package com.lord.small_box.services;

import java.math.BigDecimal;
import java.util.List;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;

public interface ContainerService {

	public List<Container> findAll();
	
	public Container createContainer(Container container);
	
	public Container findContainerByIdWithResponsible(Long id);
	
	public void deleteById(Long id);
	
	public List<Container> findAllByOrganizations(List<Organization> organizations);
	
	public List<ContainerDto> findAllbyOrganizationsByUser(Long userId);
	
	public String setContainerTotalWrite(Long containerId,String totalWrite);
	
	public Container update(Container container);
	
	public BigDecimal getSmallBoxMaxAmount(Long containerId);
}
