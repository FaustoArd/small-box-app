package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.OrganizationResponsibleDto;
import com.lord.small_box.models.OrganizationResponsible;

public interface OrganizationResponsibleService {

	public OrganizationResponsibleDto save(OrganizationResponsibleDto organizationResponsibleDto);
	
	public OrganizationResponsibleDto update(OrganizationResponsibleDto organizationResponsibleDto);
	
	public OrganizationResponsibleDto findById(Long id);
	
	public List<OrganizationResponsibleDto> findAll();
	
	
}
