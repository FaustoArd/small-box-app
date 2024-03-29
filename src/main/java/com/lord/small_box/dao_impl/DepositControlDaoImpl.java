package com.lord.small_box.dao_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.repositories.DepositControlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositControlDaoImpl implements DepositControlDao {

	@Autowired
	private final DepositControlRepository depositControlRepository;

	@Override
	public DepositControl saveDepositControl(DepositControl depositControl) {
		return depositControlRepository.save(depositControl);
	}

	@Override
	public DepositControl findDepositControlById(Long id) {
		return depositControlRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el control de deposito"));
	}

	@Override
	public void deleteDepositControlById(Long id) {
	if(depositControlRepository.existsById(id)) {
		depositControlRepository.deleteById(id);
	}else {
		throw new ItemNotFoundException("No se encontro el control de deposito");
	}
		
	}

	@Override
	public List<DepositControl> findallDepositControls() {
		return (List<DepositControl>)depositControlRepository.findAll();
	}

	@Override
	public List<DepositControl> findAllByItemCode(List<String> itemCodeIds) {
	return (List<DepositControl>)depositControlRepository.findAllByItemCodeIn(itemCodeIds);
	}
}
