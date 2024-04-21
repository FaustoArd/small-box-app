package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.Organization;

public interface BigBagRepository extends JpaRepository<BigBag, Long> {

	public List<BigBag> findAllByOrganization(Organization organization,Sort sort);
}
