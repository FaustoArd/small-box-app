package com.lord.small_box.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.Organization;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

	public List<Deposit> findAllByOrganization(Organization organization);
	
	public Optional<Deposit> findByOrganization(Organization organization); 
}
