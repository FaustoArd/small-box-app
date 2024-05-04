package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.DepositReceiver;
import com.lord.small_box.models.Organization;

public interface DepositReceiverRepository extends JpaRepository<DepositReceiver, Long> {

	public List<DepositReceiver> findAllByOrganization(Organization organization,Sort sort);
}
