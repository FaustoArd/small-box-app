package com.lord.small_box.services_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyControlServiceImpl {
	
	@Autowired
	private final DepositControlDao depositControlDao;
	
	@Autowired
	private final PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private final PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private final SupplyDao supplyDao;
	
	@Autowired
	private final SupplyItemDao supplyItemDao;

}
