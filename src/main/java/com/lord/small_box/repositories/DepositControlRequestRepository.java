package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.DepositControlRequest;
import com.lord.small_box.models.DepositRequest;

public interface DepositControlRequestRepository extends JpaRepository<DepositControlRequest, Long> {
	
	public List<DepositControlRequest> findAllByDepositRequest(DepositRequest depositRequest);

}
