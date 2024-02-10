package com.lord.small_box.services_impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.WorkTemplateRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.WorkTemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkTemplateServiceImpl implements WorkTemplateService {
	
	@Autowired
	private final WorkTemplateRepository workTemplateRepository;
	
	@Autowired
	private final OrganizationService organizationService;
	
	 

	@Override
	public WorkTemplate createTemplate(WorkTemplate workTemplate) {
		Organization org = organizationService.findById(workTemplate.getOrganization().getId());
	workTemplate.setOrganization(org);
	return workTemplateRepository.save(workTemplate);
				
		
	}

	@Override
	public WorkTemplate findWorkTemplateById(Long id) {
	return workTemplateRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el template de trabajo"));
	
	}

	@Override
	public List<WorkTemplate> findAllWorkTemplatesByOrganization(Long organizationId) {
		Organization org = organizationService.findById(organizationId);
	return (List<WorkTemplate>)workTemplateRepository.findAllWorkTemplatesByOrganization(org);
	}

	@Override
	public List<WorkTemplate> finalAllWorkTemplatesByOrganizationsId(Long userId) {
		List<Long> organizationsId = organizationService.findAllOrganizationsByUsers(userId).stream().map(org -> org.getId()).toList();
		List<WorkTemplate> workTemplates = workTemplateRepository.findAllWorkTemplatesByOrganizationIn(organizationsId);
		return workTemplates;
	}

}
