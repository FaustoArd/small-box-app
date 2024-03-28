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
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.SupplyControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyControlServiceImpl implements SupplyControlService {
	
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
		PurchaseOrderDto purchaseOrderDto = textToPurchaseOrder.textToPurchaseOrder(text);
		List<PurchaseOrderItem> items = purchaseOrderItemDao
				.saveAll(PurchaseOrderItemMapper.INSTANCE.dtoToItems(purchaseOrderDto.getItems()));
		PurchaseOrder purchaseOrder = PurchaseOrderMapper.INSTANCE.dtoToOrder(purchaseOrderDto);
		Organization execUnit = organizationService.findById(2L);
		Organization dependency = organizationService.findById(2L);
		purchaseOrder.setItems(items);
		purchaseOrder.setExecuterUnit(execUnit);
		purchaseOrder.setDependency(dependency);
		PurchaseOrder savedOrder = purchaseOrderDao.savePurchaseOrder(purchaseOrder);
		PurchaseOrderDto orderDto = PurchaseOrderMapper.INSTANCE.orderToDto(savedOrder);
		orderDto.setItems(PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items));
		return orderDto;
	}

	@Override
	public PurchaseOrderDto findFullPurchaseOrder(Long id) {
		PurchaseOrder order = purchaseOrderDao.findPurchaseOrderById(id);
		List<PurchaseOrderItem> items = purchaseOrderItemDao
				.findAllbyId(order.getItems().stream().map(m -> m.getId()).toList());
		List<PurchaseOrderItemDto> itemsDto = PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items);
		PurchaseOrderDto purchaseOrderDto = PurchaseOrderMapper.INSTANCE.orderToDto(order);
		purchaseOrderDto.setItems(itemsDto);
		return purchaseOrderDto;
	}

}
