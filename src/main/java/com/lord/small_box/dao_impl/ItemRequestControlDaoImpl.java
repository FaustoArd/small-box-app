package com.lord.small_box.dao_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.ItemRequestControlDao;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.ItemRequestControl;
import com.lord.small_box.repositories.ItemRequestControlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemRequestControlDaoImpl implements ItemRequestControlDao {

	@Autowired
	private final ItemRequestControlRepository itemRequestControlRepository;

	@Override
	public ItemRequestControl saveItemRequestControl(ItemRequestControl itemRequestControl) {
		return itemRequestControlRepository.save(itemRequestControl);
	}

	@Override
	public ItemRequestControl findItemRequestControlById(Long id) {
		return itemRequestControlRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el pedido de item"));
	}

	@Override
	public void deleteItemRequestControlById(Long id) {
		if (itemRequestControlRepository.existsById(id)) {
			itemRequestControlRepository.deleteById(id);
		} else {
			throw new ItemNotFoundException("No se encontro el pedido de item");
		}

	}

	@Override
	public List<ItemRequestControl> findAllItemRequestControls() {
		return (List<ItemRequestControl>) itemRequestControlRepository.findAll();

	}

}
