package com.lord.small_box.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;

public interface OrganizationResponsibleRepository extends JpaRepository<OrganizationResponsible, Long> {
	
	public Optional<OrganizationResponsible> findByOrganization(Organization organization);

}
