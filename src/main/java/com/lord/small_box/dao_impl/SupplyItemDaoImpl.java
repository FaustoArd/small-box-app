package com.lord.small_box.dao_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyItemDaoImpl implements SupplyItemDao {
	
	@Autowired
	private final SupplyItemRepository supplyItemRepository;
	
	

	@Override
	public SupplyItem findSupplyItemById(Long id) {
		return supplyItemRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el item de suministro"));
	}

	@Override
	public SupplyItem saveSupplyItem(SupplyItem supplyItem) {
		return supplyItemRepository.save(supplyItem);
	}

	@Override
	public void deleteSupplyItemById(Long id) {
		if(supplyItemRepository.existsById(id)) {
			supplyItemRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("No se encontro el item de suministro");
		}
		
	}

	@Override
	public List<SupplyItem> findallSupplyItems() {
		return (List<SupplyItem>)supplyItemRepository.findAll();
	}

	@Override
	public List<SupplyItem> saveAll(List<SupplyItem> items) {
		return (List<SupplyItem>)supplyItemRepository.saveAll(items);
	}

	@Override
	public List<SupplyItem> findAllBySupply(Supply supply) {
		return (List<SupplyItem>)supplyItemRepository.findAllBySupply(supply);
	}

}
