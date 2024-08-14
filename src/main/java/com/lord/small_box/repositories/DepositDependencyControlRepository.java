package com.lord.small_box.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositDependencyControl;
import com.lord.small_box.models.Organization;

public interface DepositDependencyControlRepository extends JpaRepository<DepositDependencyControl, Long> {

	public List<DepositDependencyControl> findAllByApplicantOrganizationAndDeposit(Organization organization,Deposit deposit);
	
	public List<DepositDependencyControl> findAllByDeposit(Deposit deposit);
	
	public Optional<DepositDependencyControl> findByItemCodeAndApplicantOrganizationAndDeposit(String itemCode,Organization applicantOrganization
			,Deposit deposit);
}
