package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;

public interface OrganizationService {

	public List<OrganizationDto> findAll();
	
	public Organization save(Organization organization);
	
	public Organization update(Organization organization);
	
	public Organization findById(Long id);
	
	public void deleteById(Long id);
	
	public String addOrganizationToUser(Long userId,List<Long> organizationsId);
	
	public List<OrganizationDto> findOrganizationByUser(Long userId);
	
	public List<OrganizationDto> findAllById(List<Long> organizationsId);
	
	public List<Long> getAllOrganizationsIdByUserId(Long userId);
	
	public List<Organization> findAllOrganizationsByUsers(Long userId);
}