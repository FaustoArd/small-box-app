package com.lord.small_box.services_impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyCorrectionNote;
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
	public PurchaseOrderDto collectPurchaseOrderFromText(String text,long organizationId) {
		PurchaseOrderDto purchaseOrderDto = textToPurchaseOrder.textToPurchaseOrder(text, organizationService);
		PurchaseOrder purchaseOrder = PurchaseOrderMapper.INSTANCE.dtoToOrder(purchaseOrderDto);
		Organization org = organizationService.findById(organizationId);
		purchaseOrder.setOrganization(org);
		List<PurchaseOrderItem> items = PurchaseOrderItemMapper.INSTANCE.dtoToItems(purchaseOrderDto.getItems());
		Organization execUnit = organizationService.findById(purchaseOrderDto.getExecuterUnitOrganizationId());
		Organization dependency = organizationService.findById(purchaseOrderDto.getDependencyOrganizacionId());
		purchaseOrder.setExecuterUnit(execUnit);
		purchaseOrder.setDependency(dependency);
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

	@Override
	public List<String> loadPurchaseOrderToDepositControl(Long purchaseOrderId) {
		List<PurchaseOrderItem> items = purchaseOrderItemDao
				.findAllByPurchaseOrder(purchaseOrderDao.findPurchaseOrderById(purchaseOrderId));
		List<String> report = new ArrayList<>();
		List<DepositControl> collectedItems = items.stream().map(orderItem -> {
			Optional<DepositControl> opt = depositControlDao.findByItemCode(orderItem.getCode());
			if (opt.isPresent()) {
				DepositControl depositControl = opt.get();
				depositControl.setQuantity(depositControl.getQuantity() + orderItem.getQuantity());
				report.add("Se actualizo el item: " + depositControl.getItemName() + ", cantidad total: "
						+ depositControl.getQuantity() + ", unidad de medida: " + depositControl.getMeasureUnit());
				return depositControl;

			} else {
				DepositControl depositControl = new DepositControl();
				depositControl.setItemCode(orderItem.getCode());
				depositControl.setItemName(orderItem.getItemDetail());
				depositControl.setItemTotalPrice(orderItem.getEstimatedCost());
				depositControl.setItemUnitPrice(orderItem.getUnitCost());
				depositControl.setQuantity(orderItem.getQuantity());
				depositControl.setMeasureUnit(orderItem.getMeasureUnit());
				report.add("Se creo un nuevo item: " + depositControl.getItemName() + ", cantidad total: "
						+ depositControl.getQuantity() + ", unidad de medida: " + depositControl.getMeasureUnit());
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
	public SupplyCorrectionNote createSupplyCorrectionNote(long supplyId) {
		Supply supply = supplyDao.findSupplyById(supplyId);
		List<SupplyItem> supplyItems = supplyItemDao.findAllBySupply(supply);
		List<SupplyReportDto> reportDtos = getSupplyReport(supplyItems);
		Organization org = organizationService.findById(1L);
		String to = organizationService.findById(supply.getDependencyApplicant().getId()).getOrganizationName();
		SupplyCorrectionNote supplyCorrectionNote = new SupplyCorrectionNote();
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
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode(depositItem.get().getItemCode());
				supplyReportDto.setDepositItemDetail(depositItem.get().getItemName());
				supplyReportDto.setDepositItemQuantity(depositItem.get().getQuantity());
				supplyReportDto.setDepositQuantityLeft(depositItem.get().getQuantity() - supplyItem.getQuantity());
				return supplyReportDto;
			} else {

				supplyReportDto.setSupplyItemCode(supplyItem.getCode());
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode("No encontrado");
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

}
