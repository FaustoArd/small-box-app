package com.lord.small_box.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.DepositControl;

public interface DepositControlRepository extends JpaRepository<DepositControl, Long> {
	
	public List<DepositControl> findAllByItemCodeIn(List<String> itemCodes);
	
	public Optional<DepositControl> findByItemCode(String itemCode);

}
