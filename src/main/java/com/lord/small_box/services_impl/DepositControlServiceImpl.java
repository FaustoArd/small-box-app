package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.mappers.DepositControlMapper;
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
	public PurchaseOrderDto collectPurchaseOrderFromText(String text,long organizationId) {
		PurchaseOrderDto purchaseOrderDto = textToPurchaseOrder.textToPurchaseOrder(text, organizationService);
		PurchaseOrder purchaseOrder = PurchaseOrderMapper.INSTANCE.dtoToOrder(purchaseOrderDto);
		Organization org = organizationService.findById(organizationId);
		purchaseOrder.setOrganization(org);
		List<PurchaseOrderItem> items = PurchaseOrderItemMapper.INSTANCE.dtoToItems(purchaseOrderDto.getItems());
		PurchaseOrder savedOrder = purchaseOrderDao.savePurchaseOrder(purchaseOrder);
		List<PurchaseOrderItem> updatedItems = items.stream().map(m -> {
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

	@Transactional
	@Override
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId) {
		PurchaseOrder order = purchaseOrderDao.findPurchaseOrderById(purchaseOrderId);
		Organization org = organizationService.findById(order.getOrganization().getId());
		List<PurchaseOrderItem> items = purchaseOrderItemDao
				.findAllByPurchaseOrder(purchaseOrderDao.findPurchaseOrderById(purchaseOrderId));
		List<PurchaseOrderToDepositReportDto> report = new ArrayList<>();
		List<DepositControl> collectedItems = items.stream().map(orderItem -> {
			Optional<DepositControl> opt = depositControlDao.findByItemCode(orderItem.getCode());
			if (opt.isPresent()) {
				DepositControl depositControl = opt.get();
				depositControl.setQuantity(depositControl.getQuantity() + orderItem.getQuantity());
				depositControl.setItemTotalPrice(depositControl.getItemUnitPrice()
						.multiply(new BigDecimal(depositControl.getQuantity())));
				report.add(new PurchaseOrderToDepositReportDto(depositControl.getItemName()
						,depositControl.getQuantity() , depositControl.getMeasureUnit(),"ACTUALIZADO"));
				order.setLoadedToDeposit(true);
				purchaseOrderDao.savePurchaseOrder(order);
				return depositControl;

			} else {
				DepositControl depositControl = new DepositControl();
				depositControl.setItemCode(orderItem.getCode());
				depositControl.setItemName(orderItem.getItemDetail());
				depositControl.setQuantity(orderItem.getQuantity());
				depositControl.setItemTotalPrice(orderItem.getUnitCost().multiply(new BigDecimal(depositControl.getQuantity())));
				depositControl.setItemUnitPrice(orderItem.getUnitCost());
				
				depositControl.setMeasureUnit(orderItem.getMeasureUnit());
				depositControl.setOrganization(org);
				report.add(new PurchaseOrderToDepositReportDto(depositControl.getItemName()
						, depositControl.getQuantity(), depositControl.getMeasureUnit(),"NUEVO"));
				order.setLoadedToDeposit(true);
				purchaseOrderDao.savePurchaseOrder(order);
				return depositControl;
			}

		}).toList();
		depositControlDao.saveAll(collectedItems);
		return report;
	}

	@Override
	public SupplyDto collectSupplyFromText(String text,long organizationId) {
		SupplyDto supplyDto = textToSupply.textToSupply(text, organizationService);
		Organization org = organizationService.findById(organizationId);
		Supply supply = SupplyMapper.INSTANCE.dtoToSupply(supplyDto);
		supply.setOrganization(org);
		Supply savedSupply = supplyDao.saveSupply(supply);
		
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
	public List<SupplyReportDto> createSupplyReport(long supplyId) {
		Supply supply = supplyDao.findSupplyById(supplyId);
		List<SupplyItem> supplyItems = supplyItemDao.findAllBySupply(supply);
		return getSupplyReport(supplyItems);
	}

	@Override
	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId) {
		Supply supply = supplyDao.findSupplyById(supplyId);
		List<SupplyItem> supplyItems = supplyItemDao.findAllBySupply(supply);
		List<SupplyReportDto> reportDtos = getSupplyReport(supplyItems);
		Organization org = organizationService.findById(1L);
		String to = supply.getDependencyApplicant();
		SupplyCorrectionNoteDto supplyCorrectionNote = new SupplyCorrectionNoteDto();
		supplyCorrectionNote.setSupplyNumber(supply.getSupplyNumber());
		supplyCorrectionNote.setFrom(org.getOrganizationName());
		supplyCorrectionNote.setTo(to);
		supplyCorrectionNote.setSupplyReport(reportDtos);
		return supplyCorrectionNote;
	}

	private List<SupplyReportDto> getSupplyReport(List<SupplyItem> supplyItems) {
		List<SupplyReportDto> report = supplyItems.stream().map(supplyItem -> {
			SupplyReportDto supplyReportDto = new SupplyReportDto();
			Optional<DepositControl> depositItem = depositControlDao.findByItemCode(supplyItem.getCode());
			if (depositItem.isPresent()) {
				supplyReportDto.setSupplyItemCode(supplyItem.getCode());
				supplyReportDto.setSupplyItemMeasureUnit(supplyItem.getMeasureUnit());
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode(depositItem.get().getItemCode());
				supplyReportDto.setDepositItemMeasureUnit(depositItem.get().getMeasureUnit());
				supplyReportDto.setDepositItemDetail(depositItem.get().getItemName());
				supplyReportDto.setDepositItemQuantity(depositItem.get().getQuantity());
				supplyReportDto.setDepositQuantityLeft(depositItem.get().getQuantity() - supplyItem.getQuantity());
				return supplyReportDto;
			} else {

				supplyReportDto.setSupplyItemCode(supplyItem.getCode());
				supplyReportDto.setSupplyItemMeasureUnit(supplyItem.getMeasureUnit());
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode("No encontrado");
				supplyReportDto.setDepositItemMeasureUnit("No encontrado");
				supplyReportDto.setDepositItemDetail("No encontrado");
				supplyReportDto.setDepositItemQuantity(0);

				return supplyReportDto;
			}
		}).toList();
		return report;
	}

	@Override
	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId) {
		Organization organization = organizationService.findById(organizationId);
		List<PurchaseOrder> orders = purchaseOrderDao.findAllByOrganization(organization);
		return  PurchaseOrderMapper.INSTANCE.ordersToDtos(orders);
		 }

	@Override
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId) {
		Organization organization = organizationService.findById(organizationId);
		List<Supply> supplies = supplyDao.findAllSuppliesByOrganization(organization);
		return SupplyMapper.INSTANCE.suppliesToDtos(supplies);
 	}

	@Override
	public List<SupplyItemDto> findSupplyItems(long supplyId) {
		Supply supply = supplyDao.findSupplyById(supplyId);
		List<SupplyItem> items = supplyItemDao.findAllBySupply(supply);
		return SupplyItemMapper.INSTANCE.itemToDtos(items);
	}

	@Override
	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId) {
		PurchaseOrder purchaseOrder = purchaseOrderDao.findPurchaseOrderById(purchaseOrderId);
		List<PurchaseOrderItem> items = purchaseOrderItemDao.findAllByPurchaseOrder(purchaseOrder);
		return PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items);
	}

	@Override
	public List<DepositControlDto> findDepositControlsByOrganization(long organizationId) {
		Organization org = organizationService.findById(organizationId);
		List<DepositControl> controls = depositControlDao.findAllbyOrganization(org);
		
		return DepositControlMapper.INSTANCE.depositControlsToDtos(controls);
	}

}
