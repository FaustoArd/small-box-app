package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.WorkTemplateDto;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;

public interface WorkTemplateService {

	public WorkTemplate createTemplate(WorkTemplate workTemplate);
	
	public WorkTemplate findWorkTemplateById(Long id);
	
	public List<WorkTemplate> findAllWorkTemplatesByOrganization(Long organizationId);
}
