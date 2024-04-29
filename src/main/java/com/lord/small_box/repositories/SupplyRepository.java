package com.lord.small_box.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.Supply;

public interface SupplyRepository extends JpaRepository<Supply, Long>{

	public List<Supply> findAllByOrganization(Organization organization,Sort sort);
	
	public Optional<Supply> findBySupplyNumberAndOrganization(int orderNumber,Organization organization);
}
