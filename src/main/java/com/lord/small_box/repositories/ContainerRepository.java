package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;

public interface ContainerRepository extends JpaRepository<Container, Long> {

	public List<Container> findAllByOrganizationInOrderByIdAsc(List<Organization> organizations);
}
