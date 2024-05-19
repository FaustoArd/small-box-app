package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.exceptions.DuplicateItemException;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.PurchaseOrderItemMapper;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.PurchaseOrderService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	private final TextToPurchaseOrder textToPurchaseOrder;

	@Autowired
	private final PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private final PurchaseOrderItemRepository purchaseOrderItemRepository;

	@Autowired
	private final OrganizationService organizationService;

	@Autowired
	private final DepositRepository depositRepository;

	@Autowired
	private final DepositControlRepository depositControlRepository;

	Sort purchaseOrderDateSort = Sort.by("date").descending();

	private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

	@Transactional
	@Override
	public PurchaseOrderDto collectPurchaseOrderFromText(String text, long organizationId) {
		log.info("Load purchase order from text, organization id: " + organizationId);
		PurchaseOrderDto purchaseOrderDto = textToPurchaseOrder.textToPurchaseOrder(text, organizationService);
		Organization org = organizationService.findById(organizationId);
		Optional<PurchaseOrder> checkDuplicate = purchaseOrderRepository
				.findByOrderNumberAndExerciseYearAndOrganization(purchaseOrderDto.getOrderNumber(),
						purchaseOrderDto.getExerciseYear(), org);
		if (checkDuplicate.isPresent()) {
			throw new DuplicateItemException("La orden de compra con el numero: " + purchaseOrderDto.getOrderNumber()
					+ "-" + purchaseOrderDto.getExerciseYear() + " ya existe.");
		}
		PurchaseOrder purchaseOrder = PurchaseOrderMapper.INSTANCE.dtoToOrder(purchaseOrderDto);
		purchaseOrder.setOrganization(org);
		List<PurchaseOrderItem> items = PurchaseOrderItemMapper.INSTANCE.dtoToItems(purchaseOrderDto.getItems());
		PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
		List<PurchaseOrderItem> updatedItems = items.stream().map(m -> {
			m.setPurchaseOrder(savedOrder);
			return m;
		}).toList();
		purchaseOrderItemRepository.saveAll(updatedItems);
		PurchaseOrderDto orderDto = PurchaseOrderMapper.INSTANCE.orderToDto(savedOrder);
		orderDto.setItems(PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items));

		return orderDto;
	}

	@Override
	public PurchaseOrderDto findPurchaseOrder(long purchaseOrderId) {
		log.info("Find purchase order by id");
		PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la orden de compra"));
		return PurchaseOrderMapper.INSTANCE.orderToDto(order);
	}

	@Override
	public PurchaseOrderDto findFullPurchaseOrder(Long purchaseOrderId) {
		log.info("Find full purchase order by id");
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la orden"));
		List<PurchaseOrderItem> items = purchaseOrderItemRepository.findAllByPurchaseOrder(purchaseOrder);
		PurchaseOrderDto purchaseOrderDto = PurchaseOrderMapper.INSTANCE.orderToDto(purchaseOrder);
		purchaseOrderDto.setItems(PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items));

		return purchaseOrderDto;
	}

	@Override
	public int deletePurchaseOrder(long orderId) {
		log.info("Delete purchase order by id");
		if (purchaseOrderRepository.existsById(orderId)) {
			int orderNumberDeleted = purchaseOrderRepository.findById(orderId).get().getOrderNumber();
			purchaseOrderRepository.deleteById(orderId);
			return orderNumberDeleted;

		} else {
			throw new ItemNotFoundException("No se encontro la orden de compra");
		}
	}

	@Transactional
	@Override
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId,
			Long depositId) {
		log.info("Load purchase order to deposit");
		PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la orden"));
		List<PurchaseOrderItem> items = purchaseOrderItemRepository.findAllByPurchaseOrder(order);
		List<PurchaseOrderToDepositReportDto> report = new ArrayList<>();
		Deposit deposit = depositRepository.findById(depositId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
		List<DepositControl> collectedItems = items.stream().map(orderItem -> {
			Optional<DepositControl> depositControlFound = depositControlRepository
					.findByItemCodeAndDeposit(orderItem.getCode(), deposit);
			if (depositControlFound.isPresent()) {
				return updateDepositExistingItem(report, order, deposit, depositControlFound, orderItem);

			} else {
				return createNewDepositItem(report, order, deposit, orderItem);
			}
		}).toList();
		depositControlRepository.saveAll(collectedItems);
		return report;
	}

	private DepositControl updateDepositExistingItem(List<PurchaseOrderToDepositReportDto> report, PurchaseOrder order,
			Deposit deposit, Optional<DepositControl> depositControlFound, PurchaseOrderItem orderItem) {
		DepositControl depositControl = depositControlFound.get();
		depositControl.setQuantity(depositControl.getQuantity() + orderItem.getQuantity());
		depositControl.setItemTotalPrice(
				depositControl.getItemUnitPrice().multiply(new BigDecimal(depositControl.getQuantity())));
		report.add(
				new PurchaseOrderToDepositReportDto(depositControl.getItemCode(), depositControl.getItemDescription(),
						depositControl.getQuantity(), depositControl.getMeasureUnit(), "ACTUALIZADO"));
		order.setLoadedToDeposit(true);
		order.setLoadedToDepositId(deposit.getId());
		order.setLoadedToDepositName(deposit.getName());
		purchaseOrderRepository.save(order);
		return depositControl;
	}

	private DepositControl createNewDepositItem(List<PurchaseOrderToDepositReportDto> report, PurchaseOrder order,
			Deposit deposit, PurchaseOrderItem orderItem) {
		log.info("new item,creating deposit control item");
		DepositControl depositControl = new DepositControl();
		depositControl.setItemCode(orderItem.getCode());
		depositControl.setItemDescription(orderItem.getItemDetail());
		depositControl.setQuantity(orderItem.getQuantity());
		depositControl
				.setItemTotalPrice(orderItem.getUnitCost().multiply(new BigDecimal(depositControl.getQuantity())));
		depositControl.setItemUnitPrice(orderItem.getUnitCost());
		depositControl.setMeasureUnit(orderItem.getMeasureUnit());
		depositControl.setDeposit(deposit);
		log.info("Add purchase order to deposit report");
		report.add(
				new PurchaseOrderToDepositReportDto(depositControl.getItemCode(), depositControl.getItemDescription(),
						depositControl.getQuantity(), depositControl.getMeasureUnit(), "NUEVO"));
		order.setLoadedToDeposit(true);
		order.setLoadedToDepositId(deposit.getId());
		order.setLoadedToDepositName(deposit.getName());
		purchaseOrderRepository.save(order);
		return depositControl;
	}

	@Override
	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId) {
		Organization organization = organizationService.findById(organizationId);
		List<PurchaseOrder> orders = purchaseOrderRepository.findAllByOrganization(organization, purchaseOrderDateSort);
		return PurchaseOrderMapper.INSTANCE.ordersToDtos(orders);
	}

	@Override
	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId) {
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontrol la orden"));
		List<PurchaseOrderItem> items = purchaseOrderItemRepository.findAllByPurchaseOrder(purchaseOrder);
		return PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items);
	}

}
