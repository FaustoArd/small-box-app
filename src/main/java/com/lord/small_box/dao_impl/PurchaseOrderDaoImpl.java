package com.lord.small_box.dao_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.repositories.PurchaseOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderDaoImpl implements PurchaseOrderDao {
	
	@Autowired
	private final PurchaseOrderRepository purchaseOrderRepository;

	@Override
	public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
	return purchaseOrderRepository.save(purchaseOrder);
	}

	@Override
	public PurchaseOrder findPurchaseOrderById(Long id) {
		return purchaseOrderRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro la orden de compra"));
	}

	@Override
	public void deletePurchaseOrderById(Long id) {
		if(purchaseOrderRepository.existsById(id)) {
			purchaseOrderRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("No se encontro la orden de compra");
		}
		
	}

	@Override
	public List<PurchaseOrder> findAllByOrganization(Organization organization) {
		return (List<PurchaseOrder>)purchaseOrderRepository.findAllByOrganization(organization);
	}

	
	
	

}
