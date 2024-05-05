package com.lord.small_box.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.ParentOrganization;

public interface ParentOrganizationRepository extends JpaRepository<ParentOrganization, Long> {

	Optional<ParentOrganization> findByMainOrganization(Organization mainOrganization);
}
