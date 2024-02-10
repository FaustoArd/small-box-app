package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;

public interface WorkTemplateRepository extends JpaRepository<WorkTemplate, Long>{
	
	public List<WorkTemplate> findAllWorkTemplatesByOrganization(Organization organization);
	
	public List<WorkTemplate> findAllWorkTemplatesByOrganizationIn(List<Organization> organizations); 

}
