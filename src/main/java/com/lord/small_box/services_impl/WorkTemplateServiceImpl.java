package com.lord.small_box.services_impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.lord.small_box.exceptions.ItemNotFoundException;
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
	public WorkTemplate createTemplate(WorkTemplate workTemplate) {
		Organization org = organizationRepository.findById(workTemplate.getOrganization().getId())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la oganizacion"));
	workTemplate.setOrganization(org);
	return workTemplateRepository.save(workTemplate);
				
		
	}

	@Override
	public WorkTemplate findWorkTemplateById(Long id) {
	return workTemplateRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el template de trabajo"));
	
	}

	@Override
	public List<WorkTemplate> findAllWorkTemplatesByOrganization(Long organizationId) {
		Organization org = organizationRepository.findById(organizationId).orElseThrow(() -> new ItemNotFoundException("No se encontro la oganizacion"));
	return (List<WorkTemplate>)workTemplateRepository.findAllWorkTemplatesByOrganization(org);
	}

}
