package com.lord.small_box.dao;

import java.util.List;
import java.util.Optional;

import com.lord.small_box.models.DepositControl;

public interface DepositControlDao {
	
	public DepositControl saveDepositControl(DepositControl depositControl);
	
	public DepositControl findDepositControlById(Long id);
	
	public void deleteDepositControlById(Long id);
	
	public List<DepositControl> findallDepositControls();
	
	public List<DepositControl> findAllByItemCode(List<String> itemCodes);
	
	public List<DepositControl> saveAll(List<DepositControl> depositControls);
	
	public Optional<DepositControl> findByItemCode(String itemCode);
}