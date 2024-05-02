package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.DepositControlReceiver;
import com.lord.small_box.models.DepositReceiver;

public interface DepositControlReceiverRepository extends JpaRepository<DepositControlReceiver, Long> {

	public List<DepositControlReceiver> findAllByDepositReceiver(DepositReceiver depositReceiver); 
}
