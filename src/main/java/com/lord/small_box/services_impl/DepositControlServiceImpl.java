package com.lord.small_box.services_impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.mappers.PurchaseOrderItemMapper;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.mappers.SupplyItemMapper;
import com.lord.small_box.mappers.SupplyMapper;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;
import com.lord.small_box.text_analisys.TextToSupply;
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
	private final TextToSupply textToSupply;
	
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
		List<PurchaseOrderItem> items = purchaseOrderItemDao.
				findAllByPurchaseOrder(purchaseOrderDao.findPurchaseOrderById(purchaseOrderId));
		List<String> itemCodes = items.stream().map(m -> m.getCode()).toList();
	
		
		
		List<DepositControl> depositItems = depositControlDao.findAllByItemCode(itemCodes).stream()
				.map(depositItem -> {
					items.forEach(e -> {
						if(e.getCode().equals(depositItem.getItemCode())) {
							depositItem.setQuantity(depositItem.getQuantity()+e.getQuantity());
							
						}else {
							DepositControl depositControl = new DepositControl();
							depositControl.setItemCode(e.getCode());
							depositControl.setItemName(e.getItemDetail());
							depositControl.setItemUnitPrice(e.getUnitCost());
							depositControl.setQuantity(e.getQuantity());
							
						}
					});
					return depositItem;
				}).toList();
		
		List<DepositControl> savedItems=  depositControlDao.saveAll(depositItems);
		
		return "Se modificaron los siguientes items: " + savedItems.stream()
		.map(m -> m.getItemName() + "\\n").collect(Collectors.joining(""));
	}

	@Override
	public SupplyDto loadSupply(String text) {
			SupplyDto supplyDto = textToSupply.textToSupply(text,organizationService);
			Supply savedSupply = supplyDao.saveSupply(SupplyMapper.INSTANCE.dtoToSupply(supplyDto));
			List<SupplyItem> items = SupplyItemMapper.INSTANCE.dtoToItems(supplyDto.getSupplyItems());
			List<SupplyItem> savedItems = supplyItemDao.saveAll(items.stream().map(item -> {
				item.setSupply(savedSupply);
				return item;
			}).toList());
			SupplyDto supplyDtoResponse = SupplyMapper.INSTANCE.supplyToDto(savedSupply);
			supplyDtoResponse.setSupplyItems(SupplyItemMapper.INSTANCE.itemToDtos(savedItems));
			return supplyDtoResponse;
	}

	@Override
	public SupplyReportDto checkDeposit(SupplyDto supplyDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
