package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.exceptions.DuplicateItemException;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.DepositControlMapper;
import com.lord.small_box.mappers.DepositMapper;
import com.lord.small_box.mappers.PurchaseOrderItemMapper;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.mappers.SupplyItemMapper;
import com.lord.small_box.mappers.SupplyMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.DepositOrganizationSelect;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositOrganizationSelectRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;
import com.lord.small_box.text_analisys.TextToSupply;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositControlServiceImpl implements DepositControlService {

	@Autowired
	private final DepositControlRepository depositControlRepository;

	@Autowired
	private final DepositRepository depositRepository;

	@Autowired
	private final PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private final PurchaseOrderItemRepository purchaseOrderItemDao;

	@Autowired
	private final SupplyRepository supplyRepository;

	@Autowired
	private final SupplyItemRepository supplyItemRepository;

	@Autowired
	private final TextToPurchaseOrder textToPurchaseOrder;

	@Autowired
	private final TextToSupply textToSupply;

	@Autowired
	private final OrganizationService organizationService;

	@Autowired
	private final AppUserServiceImpl userService;

	@Autowired
	private DepositOrganizationSelectRepository depositOrganizationSelectRepository;

	private static final Logger log = LoggerFactory.getLogger(DepositControlService.class);

	@Override
	public PurchaseOrderDto collectPurchaseOrderFromText(String text, long organizationId) {
		log.info("Load purchase order from text, organization id: " + organizationId);
		PurchaseOrderDto purchaseOrderDto = textToPurchaseOrder.textToPurchaseOrder(text, organizationService);
		Optional<PurchaseOrder> checkDuplicate = purchaseOrderRepository
				.findByOrderNumber(purchaseOrderDto.getOrderNumber());
		if (checkDuplicate.isPresent()) {
			throw new DuplicateItemException("La orden de compra con el numero: " + purchaseOrderDto.getOrderNumber()
					+ " ya fue cargada." + " Numero de orden encontrado: " + checkDuplicate.get().getOrderNumber());
		}
		PurchaseOrder purchaseOrder = PurchaseOrderMapper.INSTANCE.dtoToOrder(purchaseOrderDto);
		Organization org = organizationService.findById(organizationId);
		purchaseOrder.setOrganization(org);
		List<PurchaseOrderItem> items = PurchaseOrderItemMapper.INSTANCE.dtoToItems(purchaseOrderDto.getItems());
		PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
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
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la orden"));
		List<PurchaseOrderItem> items = purchaseOrderItemDao.findAllByPurchaseOrder(purchaseOrder);
		PurchaseOrderDto purchaseOrderDto = PurchaseOrderMapper.INSTANCE.orderToDto(purchaseOrder);
		purchaseOrderDto.setItems(PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items));

		return purchaseOrderDto;
	}

	@Transactional
	@Override
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId,
			Long depositId) {

		PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la orden"));

		List<PurchaseOrderItem> items = purchaseOrderItemDao.findAllByPurchaseOrder(order);

		List<PurchaseOrderToDepositReportDto> report = new ArrayList<>();
		Deposit deposit = depositRepository.findById(depositId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));

		List<DepositControl> collectedItems = items.stream().map(orderItem -> {
			Optional<DepositControl> opt = depositControlRepository.findByItemCodeAndDeposit(orderItem.getCode(),
					deposit);
			if (opt.isPresent()) {
				DepositControl depositControl = opt.get();
				depositControl.setQuantity(depositControl.getQuantity() + orderItem.getQuantity());
				depositControl.setItemTotalPrice(
						depositControl.getItemUnitPrice().multiply(new BigDecimal(depositControl.getQuantity())));
				report.add(new PurchaseOrderToDepositReportDto(depositControl.getItemName(),
						depositControl.getQuantity(), depositControl.getMeasureUnit(), "ACTUALIZADO"));
				order.setLoadedToDeposit(true);
				order.setLoadedToDepositId(deposit.getId());
				order.setLoadedToDepositName(deposit.getName());
				purchaseOrderRepository.save(order);
				return depositControl;

			} else {
				DepositControl depositControl = new DepositControl();
				depositControl.setItemCode(orderItem.getCode());
				depositControl.setItemName(orderItem.getItemDetail());
				depositControl.setQuantity(orderItem.getQuantity());
				depositControl.setItemTotalPrice(
						orderItem.getUnitCost().multiply(new BigDecimal(depositControl.getQuantity())));
				depositControl.setItemUnitPrice(orderItem.getUnitCost());

				depositControl.setMeasureUnit(orderItem.getMeasureUnit());
				depositControl.setDeposit(deposit);
				report.add(new PurchaseOrderToDepositReportDto(depositControl.getItemName(),
						depositControl.getQuantity(), depositControl.getMeasureUnit(), "NUEVO"));
				order.setLoadedToDeposit(true);
				order.setLoadedToDepositId(deposit.getId());
				order.setLoadedToDepositName(deposit.getName());
				purchaseOrderRepository.save(order);
				return depositControl;
			}

		}).toList();
		depositControlRepository.saveAll(collectedItems);
		return report;
	}

	@Override
	public SupplyDto collectSupplyFromText(String text, long organizationId) {
		SupplyDto supplyDto = textToSupply.textToSupply(text, organizationService);
		Organization org = organizationService.findById(organizationId);
		Supply supply = SupplyMapper.INSTANCE.dtoToSupply(supplyDto);
		supply.setOrganization(org);
		Supply savedSupply = supplyRepository.save(supply);

		List<SupplyItem> items = SupplyItemMapper.INSTANCE.dtoToItems(supplyDto.getSupplyItems());
		List<SupplyItem> savedItems = supplyItemRepository.saveAll(items.stream().map(item -> {
			item.setSupply(savedSupply);

			return item;
		}).toList());
		SupplyDto supplyDtoResponse = SupplyMapper.INSTANCE.supplyToDto(savedSupply);
		supplyDtoResponse.setSupplyItems(SupplyItemMapper.INSTANCE.itemToDtos(savedItems));
		return supplyDtoResponse;
	}

	@Override
	public List<SupplyReportDto> createSupplyReport(long supplyId, long depositId) {
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el suministro"));
		List<SupplyItem> supplyItems = supplyItemRepository.findAllBySupply(supply);
		return getSupplyReport(supplyItems, depositId);
	}

	@Override
	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId, Long depositId) {
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el suministro"));
		List<SupplyItem> supplyItems = supplyItemRepository.findAllBySupply(supply);
		List<SupplyReportDto> reportDtos = getSupplyReport(supplyItems, depositId);
		Organization org = organizationService.findById(1L);
		String to = supply.getDependencyApplicant();
		SupplyCorrectionNoteDto supplyCorrectionNote = new SupplyCorrectionNoteDto();
		supplyCorrectionNote.setSupplyNumber(supply.getSupplyNumber());
		supplyCorrectionNote.setFrom(org.getOrganizationName());
		supplyCorrectionNote.setTo(to);
		supplyCorrectionNote.setSupplyReport(reportDtos);
		supplyCorrectionNote.setDepositName(depositRepository.findById(depositId).get().getName());
		return supplyCorrectionNote;
	}

	private List<SupplyReportDto> getSupplyReport(List<SupplyItem> supplyItems, Long depositId) {
		Deposit deposit = depositRepository.findById(depositId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
		List<SupplyReportDto> report = supplyItems.stream().map(supplyItem -> {
			SupplyReportDto supplyReportDto = new SupplyReportDto();
			Optional<DepositControl> depositItem = depositControlRepository
					.findByItemCodeAndDeposit(supplyItem.getCode(), deposit);
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
		List<PurchaseOrder> orders = purchaseOrderRepository.findAllByOrganization(organization);
		return PurchaseOrderMapper.INSTANCE.ordersToDtos(orders);
	}

	@Override
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId) {
		Organization organization = organizationService.findById(organizationId);
		List<Supply> supplies = supplyRepository.findAllByOrganization(organization);
		return SupplyMapper.INSTANCE.suppliesToDtos(supplies);
	}

	@Override
	public List<SupplyItemDto> findSupplyItems(long supplyId) {
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el suministro"));
		List<SupplyItem> items = supplyItemRepository.findAllBySupply(supply);
		return SupplyItemMapper.INSTANCE.itemToDtos(items);
	}

	@Override
	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId) {
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontrol la orden"));
		List<PurchaseOrderItem> items = purchaseOrderItemDao.findAllByPurchaseOrder(purchaseOrder);
		return PurchaseOrderItemMapper.INSTANCE.itemsToDtos(items);
	}

	@Override
	public List<DepositControlDto> findDepositControlsByDeposit(long depositId) {
		Deposit deposit = depositRepository.findById(depositId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
		List<DepositControl> controls = depositControlRepository.findAllByDeposit(deposit);

		return DepositControlMapper.INSTANCE.depositControlsToDtos(controls);
	}

	@Override
	public String createDeposit(DepositDto depositDto) {
		log.info("Create new Deposit");
		Deposit deposit = DepositMapper.INSTANCE.dtoToDeposit(depositDto);
		Organization org = organizationService.findById(depositDto.getOrganizationId());
		deposit.setOrganization(org);
		Deposit savedDeposit = depositRepository.save(deposit);
		return savedDeposit.getName();
	}

	@Override
	public List<DepositDto> findAllDepositsbyOrganization(long organizationId) {
		log.info("Find all deposit by organization");
		Organization org = organizationService.findById(organizationId);
		List<Deposit> deposits = depositRepository.findAllByOrganization(org);
		return DepositMapper.INSTANCE.depositToDtos(deposits);
	}

	@Override
	public DepositResponseDto setCurrentDeposit(long userId, long organizationId, long depositId) {
		log.info("Set current deposit id");
		AppUser user = userService.findById(userId);
		user.setCurrentDepositId(depositId);
		DepositOrganizationSelect depositOrganizationSelect = new DepositOrganizationSelect();
		depositOrganizationSelect.setDepositId(depositId);
		depositOrganizationSelect.setOrganizationId(organizationId);
		depositOrganizationSelect.setUser(user);
		depositOrganizationSelect.setDate(Calendar.getInstance());
		DepositOrganizationSelect savedDepositOrganizationSelect = depositOrganizationSelectRepository
				.save(depositOrganizationSelect);
		// AppUser updatedUser = userService.save(user);
		log.info("Set current deposit completed, deposit id: " + savedDepositOrganizationSelect.getDepositId());
		return new DepositResponseDto(savedDepositOrganizationSelect.getDepositId(),
				depositRepository.findById(depositId).get().getName());
	}

	@Override
	public DepositResponseDto getCurrentDepositId(long userId, long organizationId) {
		Sort sort = Sort.by("date").descending();
		log.info("Get current deposit id");
		AppUser user = userService.findById(userId);
		Optional<DepositOrganizationSelect> depositOrganizationSelect = depositOrganizationSelectRepository
				.findByUserAndOrganizationId(user, organizationId, sort).stream().findFirst();

		if (depositOrganizationSelect.isEmpty()) {
			log.info("User current deposit not found , returning DepositResponse(null,\"Sin Asignar\"");
			return new DepositResponseDto(null, "Sin Asignar");
		} else {
			Deposit depo = depositRepository.findById(depositOrganizationSelect.get().getDepositId())
					.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
			log.info("User current deposit found, deposit ID: " + depo.getId());
			return new DepositResponseDto(depo.getId(), depo.getName());
		}
	}

	/*
	 * @Override public DepositResponseDto resetCurrentUserSelectedDeposit(long
	 * userId, long currentOrgId) { AppUser user = userService.findById(userId);
	 * Organization org = organizationService.findById(currentOrgId);
	 * Optional<Deposit> deposit = depositRepository.findByOrganization(org);
	 * if(deposit.isPresent()) if(user.getCurrentDepositId()==deposit.get().getId())
	 * { return new DepositResponseDto(deposit.get().getId(),
	 * deposit.get().getName()); } return null; }
	 */

}
