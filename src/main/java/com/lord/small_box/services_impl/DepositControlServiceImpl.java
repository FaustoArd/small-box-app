package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.mappers.PurchaseOrderItemMapper;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositControlServiceImpl implements DepositControlService {
	
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
	
	@Autowired
	private final TextToPurchaseOrder textToPurchaseOrder;
	
	@Autowired
	private final OrganizationService organizationService;

	@Override
	public PurchaseOrderDto collectPurchaseOrderFromText(String text) {
		PurchaseOrderDto purchaseOrderDto = textToPurchaseOrder.textToPurchaseOrder(text,organizationService);
		PurchaseOrder purchaseOrder = PurchaseOrderMapper.INSTANCE.dtoToOrder(purchaseOrderDto);
		List<PurchaseOrderItem> items = PurchaseOrderItemMapper.INSTANCE.dtoToItems(purchaseOrderDto.getItems());
		
		Organization execUnit = organizationService.findById(purchaseOrderDto.getExecuterUnitOrganizationId());
		Organization dependency = organizationService.findById(purchaseOrderDto.getDependencyOrganizacionId());
		purchaseOrder.setExecuterUnit(execUnit);
		purchaseOrder.setDependency(dependency);
		PurchaseOrder savedOrder = purchaseOrderDao.savePurchaseOrder(purchaseOrder);
		List<PurchaseOrderItem> updatedItems =  items.stream().map(m ->{
			m.setPurchaseOrder(savedOrder);
			return m;
		}).toList();
		purchaseOrderItemDao.saveAll(updatedItems);
		PurchaseOrderDto orderDto = PurchaseOrderMapper.INSTANCE.orderToDto(savedOrder);
		orderDto.setItems(PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items));
		return orderDto;
	}

	@Override
	public PurchaseOrderDto findFullPurchaseOrder(Long id) {
		PurchaseOrder purchaseOrder = purchaseOrderDao.findPurchaseOrderById(id);
		List<PurchaseOrderItem> items = purchaseOrderItemDao.findAllByPurchaseOrder(purchaseOrder);
		PurchaseOrderDto purchaseOrderDto = PurchaseOrderMapper.INSTANCE.orderToDto(purchaseOrder);
		purchaseOrderDto.setItems(PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items));
		
		return purchaseOrderDto;
	}

	@Override
	public String loadPurchaseOrderToDepositControl(Long purchaseOrderId) {
		
		return null;
	}

}
