package com.lord.small_box.services_impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;
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
	public List<WorkTemplate> findAllWorkTemplatesByOrganizationByUserId(Long userId) {
		List<Organization> organizations = organizationService.findAllOrganizationsByUsers(userId);
		List<WorkTemplate> workTemplates = workTemplateRepository.findAllWorkTemplatesByOrganizationIn(organizations);
		return workTemplates;
	}

	@Override
	public List<WorkTemplate> FindWorkTemplateByExampleAndUserId(WorkTemplate workTemplate,Long userId) {
		List<Long> organizationsId = organizationService.findAllOrganizationsByUsers(userId).stream().map(org -> org.getId()).toList();
		StringMatcher match = StringMatcher.CONTAINING;
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(match).withIgnoreCase();
		Example<WorkTemplate> example = Example.of(workTemplate,matcher);
		List<WorkTemplate> results = workTemplateRepository.findAll(example).stream()
				.filter(wt -> wt.getOrganization().getId()==organizationsId.stream().map(id -> id).findAny().get()).toList();
		return null;
	}

	

	

}
