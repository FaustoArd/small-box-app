package com.lord.small_box.dao;

import java.util.List;
import com.lord.small_box.models.DepositControl;

public interface DepositControlDao {
	
	public DepositControl saveDepositControl(DepositControl depositControl);
	
	public DepositControl findDepositControlById(Long id);
	
	public void deleteDepositControlById(Long id);
	
	public List<DepositControl> findallDepositControls();

}
