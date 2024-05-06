package com.lord.small_box.services;

import java.math.BigDecimal;
import java.util.List;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.ParentOrganizationDto;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.ParentOrganization;

public interface OrganizationService {

	public List<OrganizationDto> findAll();
	
	public Organization save(Organization organization);
	
	public Organization update(Organization organization);
	
	public Organization findById(Long id);
	
	public void deleteById(Long id);
	
	public String addOrganizationToUser(Long userId,List<Long> organizationsId);
	
	/*public List<String> addOganizationReceiversToUser(long userId, List<Long> organizationReceiverIds);*/
	
	/*public List<OrganizationDto> findAllDestinationOrganizations(long userId);*/
	
	public List<OrganizationDto> findAllById(List<Long> organizationsId);
	
	public List<Organization> findAllOrganizationsByUsers(Long userId);
	
	public long setUserMainOrganization(long organizationId,long userId);
	
	public long getUserMainOrganization(long userId);
	
	public ParentOrganizationDto setParentOrganization(ParentOrganizationDto parentOrganizationDto);
	
	public List<OrganizationDto> findParentOrganizationsByMainOrganization(long mainOrganizationId);
	
}