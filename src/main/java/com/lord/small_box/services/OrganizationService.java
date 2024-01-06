package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.models.Organization;

public interface OrganizationService {

	public List<OrganizationDto> findAll();
	
	public Organization save(Organization organization);
	
	public Organization findById(Long id);
	
	public void deleteById(Long id);
	
	public String addOrganizationToUser(Long userId,List<Long> organizationsId);
}