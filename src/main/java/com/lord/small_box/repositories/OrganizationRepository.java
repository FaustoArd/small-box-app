package com.lord.small_box.repositories;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	
	public Optional<Organization> findByContainers(Container container);
	
	public List<Organization> findAllOrganizationsByUsers(AppUser user);

}
