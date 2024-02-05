package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.WorkTemplateDto;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.WorkTemplateRepository;
import com.lord.small_box.services.WorkTemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkTemplateServiceImpl implements WorkTemplateService {
	
	private final WorkTemplateRepository workTemplateRepository;
	
	private final OrganizationRepository organizationRepository;

	@Override
	public WorkTemplate createTemplate(WorkTemplateDto workTemplateDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkTemplate findWorkTemplateById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkTemplate> findAllWorkTemplatesByOrganization(Organization organization) {
		// TODO Auto-generated method stub
		return null;
	}

}
