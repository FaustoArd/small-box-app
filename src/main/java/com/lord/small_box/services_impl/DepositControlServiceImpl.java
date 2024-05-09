package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.DepositItemComparatorDto;
import com.lord.small_box.dtos.PurchaseOrderItemCandidateDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.BigBagItemMapper;
import com.lord.small_box.mappers.BigBagMapper;
import com.lord.small_box.mappers.DepositControlMapper;
import com.lord.small_box.mappers.DepositMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.BigBagItem;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.DepositOrganizationSelect;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.repositories.BigBagItemRepository;
import com.lord.small_box.repositories.BigBagRepository;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositOrganizationSelectRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;


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
	private final PurchaseOrderItemRepository purchaseOrderItemRepository;

	@Autowired
	private final DepositOrganizationSelectRepository depositOrganizationSelectRepository;

	@Autowired
	private final BigBagRepository bigBagRepository;

	@Autowired
	private final BigBagItemRepository bigBagItemRepository;

	@Autowired
	private final OrganizationService organizationService;

	@Autowired
	private final AppUserServiceImpl userService;

	private static final Logger log = LoggerFactory.getLogger(DepositControlServiceImpl.class);

	Sort purchaseOrderDateSort = Sort.by("date").descending();

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
		// user.setCurrentDepositId(depositId);
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
			log.info("User current deposit not found , returning DepositResponse(null,\"Sin Asignar\")");
			return new DepositResponseDto(null, "Sin Asignar");
		} else {
			Deposit depo = findDepositById(depositOrganizationSelect.get().getDepositId());
			log.info("User current deposit found, deposit ID: " + depo.getId());
			return new DepositResponseDto(depo.getId(), depo.getName());
		}
	}

	@Transactional
	@Override
	public BigBagDto createBigBag(BigBagDto bigBagDto, long organizationId) {
		log.info("Create BigBag");
		Organization org = organizationService.findById(organizationId);
		BigBag bigBag = BigBag.builder().name(bigBagDto.getName()).organization(org)
				.creationDate(Calendar.getInstance()).build();
		BigBag savedBigBag = bigBagRepository.save(bigBag);
		List<Long> depositControlIds = bigBagDto.getItems().stream().map(item -> item.getDepositControlId())
				.collect(Collectors.toList());
		bigBagDto.setItems(bigBagDto.getItems().stream().map(m -> {
			m.setQuantity(1);
			return m;
		}).collect(Collectors.toList()));
		List<BigBagItem> bigBagItems = dtoToBigBagItems(savedBigBag, bigBagDto.getItems(), depositControlIds);
		List<BigBagItem> items = bigBagItemRepository.saveAll(bigBagItems);
		BigBagDto dto = BigBagMapper.INSTANCE.bigBagToDto(savedBigBag);
		dto.setItems(BigBagItemMapper.INSTANCE.itemstoDtos(items));
		log.info("Se creo el bolson: " + savedBigBag.getName());
		return dto;

	}

	private List<BigBagItem> dtoToBigBagItems(BigBag bigBag, List<BigBagItemDto> bigBagItemDtos,
			List<Long> depositControlIds) {
		log.info("BigBagDto to BigBag");
		return depositControlRepository.findAllById(depositControlIds).stream().map(depoItem -> {
			BigBagItem bigBagItem = BigBagItem.builder().code(depoItem.getItemCode())
					.measureUnit(depoItem.getMeasureUnit())
					.quantity(bigBagItemDtos.stream().filter(f -> f.getDepositControlId() == depoItem.getId())
							.findFirst().get().getQuantity())
					.description(depoItem.getItemDescription()).unitCost(depoItem.getItemUnitPrice())
					.totalCost(depoItem.getItemUnitPrice().multiply(new BigDecimal(bigBagItemDtos.stream()
							.filter(f -> f.getDepositControlId() == depoItem.getId()).findFirst().get().getQuantity())))
					.bigBag(bigBag).build();
			return bigBagItem;
		}).toList();

	}

	@Override
	public int getTotalBigBagQuantityAvailable(long bigBagId, long depositId) {
		log.info("Get total bigBag quantity available");
		Deposit deposit = findDepositById(depositId);

		BigBag bigBag = findBigBagById(bigBagId);

		List<String> bigBagItemCodes = bigBagItemRepository.findByBigBag(bigBag).stream().map(m -> m.getCode())
				.collect(Collectors.toList());

		BigBagItem bigBagMaxItemQuantity = bigBagItemRepository.findByBigBag(bigBag).stream()
				.max(Comparator.comparing(BigBagItem::getQuantity))
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el item del bolson."));

		List<DepositControl> depositItems = depositControlRepository.findAllByItemCodeInAndDeposit(bigBagItemCodes,
				deposit);

		DepositControl depositMinItemQuantity = depositItems.stream()
				.min(Comparator.comparing(DepositControl::getQuantity))
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el item del deposito."));

		int result = Math.floorDiv(depositMinItemQuantity.getQuantity(), bigBagMaxItemQuantity.getQuantity());
		log.info("total bigBag quantity available: " + result);
		return result;
	}

	private static final Sort sort = Sort.by("creationDate").descending();

	@Override
	public List<BigBagDto> findAllBigBagsByOrg(long organizationId) {

		Organization org = organizationService.findById(organizationId);
		List<BigBag> bigBags = bigBagRepository.findAllByOrganization(org, sort);
		return BigBagMapper.INSTANCE.bigBagToDto(bigBags);
	}

	@Override
	public BigBagItemDto updateBigBagItemQuantity(long bigBagItemId, int quantity) {
		log.info("Update bigBag item quantity ");
		BigBagItem item = findBigBagItemById(bigBagItemId);
		item.setQuantity(quantity);
		BigBagItem updatedItem = bigBagItemRepository.save(item);
		return BigBagItemMapper.INSTANCE.itemToDto(updatedItem);
	}

	@Override
	public List<BigBagItemDto> findAllBigBagItems(long bigBagId) {
		log.info("Find All bigBag items by bigBag id.");
		BigBag bigBag = findBigBagById(bigBagId);
		List<BigBagItem> items = bigBagItemRepository.findByBigBag(bigBag);
		return BigBagItemMapper.INSTANCE.itemstoDtos(items);
	}

	@Override
	public List<DepositItemComparatorDto> getExcelToPuchaseOrderComparator(List<ExcelItemDto> excelItems,
			long organizationId) {
		log.info("Get excel data to list <DepositItemComparatorDto>.");
		Organization org = organizationService.findById(organizationId);
		List<DepositItemComparatorDto> comparators = getDepositComparator(org, excelItems);
		return comparators;
	}

	private List<DepositItemComparatorDto> getDepositComparator(Organization organization,
			List<ExcelItemDto> excelItemDtos) {
		log.info("Get deposit comparator private method");
		List<DepositItemComparatorDto> comparators = excelItemDtos.stream().map(xlsItemDto -> {
			DepositItemComparatorDto comparatorDto = new DepositItemComparatorDto();
			String strItemDesc = xlsItemDto.getItemDescription().split(" ")[0];
			Pattern pItemDesc = Pattern.compile("^(?=.*(" + strItemDesc + "))", Pattern.CASE_INSENSITIVE);
			ExcelItemDto createdExcelItemDto = new ExcelItemDto();
			List<PurchaseOrderItemCandidateDto> orderItemCandidates = getPurchaseOrderItemCandidateDto(organization,
					pItemDesc, xlsItemDto);

			Optional<PurchaseOrderItemCandidateDto> orderItemCandidateCheck = orderItemCandidates.stream()
					.filter(f -> f.getExcelItemDtoId() == xlsItemDto.getExcelItemId()).findFirst();

			return mapExcelAndDepositItemToComparatorDto(orderItemCandidateCheck, createdExcelItemDto, comparatorDto,
					xlsItemDto, orderItemCandidates);

		}).toList();
		return comparators;
	}

	private static DepositItemComparatorDto mapExcelAndDepositItemToComparatorDto(
			Optional<PurchaseOrderItemCandidateDto> orderItemCandidateCheck, ExcelItemDto createdExcelItemDto,
			DepositItemComparatorDto comparatorDto, ExcelItemDto xlsItemDto,
			List<PurchaseOrderItemCandidateDto> orderItemCandidates) {
		if (orderItemCandidateCheck.isPresent()) {
			createdExcelItemDto.setExcelItemId(xlsItemDto.getExcelItemId());
			createdExcelItemDto.setItemDescription(xlsItemDto.getItemDescription());
			createdExcelItemDto.setItemMeasureUnit(xlsItemDto.getItemMeasureUnit());
			createdExcelItemDto.setItemQuantity(xlsItemDto.getItemQuantity());
			comparatorDto.setExcelItemDto(createdExcelItemDto);
			orderItemCandidates = orderItemCandidates.stream().filter(f -> f.getExcelItemDtoId() != 0).toList();
			comparatorDto.setPurchaseOrderItemCandidateDtos(orderItemCandidates);
		} else {
			createdExcelItemDto.setExcelItemId(xlsItemDto.getExcelItemId());
			createdExcelItemDto.setItemDescription(xlsItemDto.getItemDescription());
			createdExcelItemDto.setItemMeasureUnit(xlsItemDto.getItemMeasureUnit());
			createdExcelItemDto.setItemQuantity(xlsItemDto.getItemQuantity());
			comparatorDto.setExcelItemDto(createdExcelItemDto);
			PurchaseOrderItemCandidateDto candidateNotFound = new PurchaseOrderItemCandidateDto();
			candidateNotFound.setItemDetail("No encontrado");
			comparatorDto.setPurchaseOrderItemCandidateDtos(List.of(candidateNotFound));
		}
		return comparatorDto;
	}

	private List<PurchaseOrderItemCandidateDto> getPurchaseOrderItemCandidateDto(Organization organization,
			Pattern pItemDesc, ExcelItemDto xlsItemDto) {
		log.info("Get purchase order items to candidate dtos");
		List<PurchaseOrderItemCandidateDto> orderItemCandidates = purchaseOrderItemRepository
				.findAllByPurchaseOrderIn(
						purchaseOrderRepository.findAllByOrganization(organization, purchaseOrderDateSort))
				.stream().map(orderItem -> {
					if (pItemDesc.matcher(orderItem.getItemDetail()).find()) {
						PurchaseOrderItemCandidateDto orderItemCandidateDto = purchaseOrderItemToCandidateDto(orderItem,
								xlsItemDto.getExcelItemId());
						return orderItemCandidateDto;
					}
					return new PurchaseOrderItemCandidateDto();
				}).toList();
		return orderItemCandidates;
	}

	private PurchaseOrderItemCandidateDto purchaseOrderItemToCandidateDto(PurchaseOrderItem purchaseOrderItem,
			long xlsItemId) {
		if (purchaseOrderItem == null) {
			return null;
		}
		PurchaseOrderItemCandidateDto dto = new PurchaseOrderItemCandidateDto();
		dto.setOrderId(purchaseOrderItem.getId());
		dto.setExcelItemDtoId(xlsItemId);
		dto.setCode(purchaseOrderItem.getCode());
		dto.setProgramaticCategory(purchaseOrderItem.getProgramaticCat());
		dto.setQuantity(purchaseOrderItem.getQuantity());
		dto.setItemDetail(purchaseOrderItem.getItemDetail());
		dto.setMeasureUnit(purchaseOrderItem.getMeasureUnit());
		return dto;
	}

	@Override
	public List<DepositControlDto> saveExcelItemsToDepositControls(long organizationId, long depositId,
			List<ExcelItemDto> excelItemDtos) {
		log.info("Save excel item to deposit");
		Deposit deposit = findDepositById(depositId);

		List<DepositControl> depositControlItems = purchaseOrderItemRepository
				.findAllByIdIn(excelItemDtos.stream().map(m -> m.getPurchaseOrderId()).toList()).stream()
				.map(orderItem -> {
					return mapExcelItemToDeposit(excelItemDtos, orderItem, deposit);
				}).toList();

		List<DepositControl> savedDepositControls = depositControlRepository.saveAll(depositControlItems);
		return DepositControlMapper.INSTANCE.depositControlsToDtos(savedDepositControls);

	}

	private DepositControl mapExcelItemToDeposit(List<ExcelItemDto> excelItemDtos, PurchaseOrderItem orderItem,
			Deposit deposit) {
		log.info("Map excel items to deposit");
		DepositControl depositControl = excelItemDtos.stream()
				.filter(excelItemDto -> excelItemDto.getPurchaseOrderId() == orderItem.getId()).map(excelItemDto -> {
					Optional<DepositControl> checkRepeated = depositControlRepository
							.findByItemCodeAndDeposit(orderItem.getCode(), deposit);
					if (checkRepeated.isPresent()) {
						return updateDepositControlQuantity(checkRepeated.get(), excelItemDto, deposit);
					}
					return excelItemToDepositControl(excelItemDto, orderItem, deposit);
				}).findFirst().get();

		return depositControl;
	}

	private static DepositControl updateDepositControlQuantity(DepositControl control, ExcelItemDto excelItemDto,
			Deposit deposit) {
		log.info("item exist. updating quantity");
		control.setQuantity(control.getQuantity() + excelItemDto.getItemQuantity());
		control.setDeposit(deposit);
		return control;
	}

	private static DepositControl excelItemToDepositControl(ExcelItemDto excelItemDto, PurchaseOrderItem orderItem,
			Deposit deposit) {
		log.info("creating new deposit item");
		DepositControl control = new DepositControl();
		control.setItemCode(orderItem.getCode());
		control.setMeasureUnit(orderItem.getMeasureUnit());
		control.setItemDescription(orderItem.getItemDetail());
		control.setItemUnitPrice(orderItem.getUnitCost());
		control.setQuantity(excelItemDto.getItemQuantity());
		control.setItemTotalPrice(orderItem.getUnitCost().multiply(new BigDecimal(excelItemDto.getItemQuantity())));
		control.setDeposit(deposit);
		return control;
	}

	@Override
	public List<DepositControlDto> findDepositControlsByDeposit(long depositId) {
		log.info("Find deposit controls by deposit");
		Deposit deposit = findDepositById(depositId);
		List<DepositControl> controls = depositControlRepository.findAllByDeposit(deposit);

		return DepositControlMapper.INSTANCE.depositControlsToDtos(controls);
	}

	@Override
	public String deleteDepositControlById(long depositControlId) {
		log.info("Delete deposit controls by id");
		DepositControl control = depositControlRepository.findById(depositControlId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
		depositControlRepository.deleteById(depositControlId);
		return control.getItemCode();
	}

	@Override
	public DepositControlDto findDepositControlById(long depositControlId) {
		log.info("find deposit control by id");
		DepositControl depositControl = depositControlRepository.findById(depositControlId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el item de deposito"));
		return DepositControlMapper.INSTANCE.depositControlToDto(depositControl);
	}

	@Override
	public DepositControlDto updateDepositControl(DepositControlDto depositControlDto, long depositId) {
		log.info("update deposit control. Deposit id: " + depositId);
		Deposit deposit = depositRepository.findById(depositId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
		DepositControl depositControl = DepositControlMapper.INSTANCE.dtoToDepositControl(depositControlDto);
		depositControl.setDeposit(deposit);
		DepositControl savedControl = depositControlRepository.save(depositControl);
		return DepositControlMapper.INSTANCE.depositControlToDto(savedControl);
	}
	
	private Deposit findDepositById(long depositId) {
		return depositRepository.findById(depositId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el deposito"));
	}
	
	private BigBag findBigBagById(long bigBagId) {
		return bigBagRepository.findById(bigBagId)
		.orElseThrow(() -> new ItemNotFoundException("No se encontro el bolson."));
	}
	
	private BigBagItem findBigBagItemById(long bigBagItemId) {
		return bigBagItemRepository.findById(bigBagItemId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el item del bolson."));
	}

}
