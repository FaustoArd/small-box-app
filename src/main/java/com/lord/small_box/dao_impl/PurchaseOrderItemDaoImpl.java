package com.lord.small_box.dao_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderItemDaoImpl implements PurchaseOrderItemDao {

	@Autowired
	private final PurchaseOrderItemRepository purchaseOrderItemRepository;

	@Override
	public PurchaseOrderItem findItembyId(Long id) {
		return purchaseOrderItemRepository.findById(id)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro el item de la orden de compra"));
	}

	@Override
	public PurchaseOrderItem saveItem(PurchaseOrderItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItemById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PurchaseOrderItem> findAllItems() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
