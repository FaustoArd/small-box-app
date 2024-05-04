package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.DepositRequest;
import com.lord.small_box.models.Organization;

public interface DepositRequestRepository extends JpaRepository<DepositRequest, Long> {

	public List<DepositRequest> findAllRequestByMainOrganizationIn(List<Organization> organizations,Sort sort);
}
