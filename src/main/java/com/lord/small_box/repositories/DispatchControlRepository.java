package com.lord.small_box.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.Organization;


public interface DispatchControlRepository extends JpaRepository<DispatchControl, Long>{
	
	public List<DispatchControl> findAllDistpachControlsByOrganization(Organization organization);
	
	public List<DispatchControl> findAllDistpachControlsByOrganizationIn(List<Organization> organizations); 
	
	public List<DispatchControl> findAllDispatchControlsByOrganization(Organization organization,Pageable pageable);

}
