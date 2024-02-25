package com.lord.small_box.services;

import java.util.List;

import org.springframework.data.domain.Example;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;

public interface WorkTemplateService {

	public WorkTemplate createTemplate(WorkTemplate workTemplate);
	
	public WorkTemplate findWorkTemplateById(Long id);
	
	public List<WorkTemplate> findAllWorkTemplatesByOrganization(Long organizationId);
	
	public List<WorkTemplate> findAllWorkTemplatesByOrganizationByUserId(Long userId);
	
	public List<WorkTemplate> FindWorkTemplateByExampleAndUserId(WorkTemplate example,Long userId);
	
	public String deleteWorkTemplateById(Long id);
}
