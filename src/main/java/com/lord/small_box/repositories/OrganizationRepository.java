package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	
	

}
