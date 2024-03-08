package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.Organization;

public interface DispatchControlService {

	
	public String createDispatch(DispatchControl dispatchControl);
	
	public List<DispatchControl> findAll();
	
	public DispatchControl findById(Long id);
	
	public String deleteById(Long id);
	
	public List<DispatchControl> findAllDistpachControlsByOrganizationIn(List<Organization> organizations); 
	
	public List<DispatchControl> findAllDistpachControlsByOrganization(Long organizationId);
	
	public String dispatchWorkTemplate(Long workTemplateId);
	
	
		
	
}
