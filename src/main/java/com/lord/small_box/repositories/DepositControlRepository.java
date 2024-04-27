package com.lord.small_box.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;

public interface DepositControlRepository extends JpaRepository<DepositControl, Long> {
	
	public List<DepositControl> findAllByItemCodeInAndDeposit(List<String> itemCodes,Deposit deposit);
	
	public Optional<DepositControl> findByItemCodeAndDeposit(String itemCode,Deposit deposit);
	
	public List<DepositControl> findAllByDeposit(Deposit deposit);
	
	
	

}
