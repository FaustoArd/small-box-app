package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.LocationContract;
import com.lord.small_box.models.Organization;

public interface LocationContractRepository extends JpaRepository<LocationContract, Long>{
	
	
	public List<LocationContract> findAllLocationContractsByOrganizationIn(List<Organization> organizations);
}
