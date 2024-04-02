package com.lord.small_box.dao_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.Supply;
import com.lord.small_box.repositories.SupplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyDaoImpl implements SupplyDao {

	@Autowired
	private final SupplyRepository supplyRepository;

	@Override
	public Supply findSupplyById(Long id) {
		return supplyRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el suministro"));
	}

	@Override
	public Supply saveSupply(Supply supply) {
	return supplyRepository.save(supply);
	}

	@Override
	public void deleteSupplyById(Long id) {
	if(supplyRepository.existsById(id)) {
		supplyRepository.deleteById(id);
	}else {
		throw new ItemNotFoundException("No se encontro el suministro");
	}

	}

	@Override
	public List<Supply> findAllSupplies() {
		return (List<Supply>)supplyRepository.findAll();
	}

	@Override
	public List<Supply> findAllSuppliesByOrganization(Organization organization) {
		return (List<Supply>)supplyRepository.findAllByOrganization(organization);
	}

}
