package com.lord.small_box.services_impl;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyItemRequestDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.exceptions.DuplicateItemException;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.SupplyItemMapper;
import com.lord.small_box.mappers.SupplyMapper;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.SupplyService;
import com.lord.small_box.text_analisys.TextToSupply;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

	
	@Autowired
	private final SupplyRepository supplyRepository;

	@Autowired
	private final SupplyItemRepository supplyItemRepository;
	
	@Autowired
	private final OrganizationService organizationService;
	
	@Autowired
	private final TextToSupply textToSupply;
	
	@Autowired
	private final DepositControlRepository depositControlRepository;
	
	@Autowired
	private final DepositRepository depositRepository;
	
	private static final Logger log = LoggerFactory.getLogger(SupplyServiceImpl.class);
	
	private final Sort supplyDatesort = Sort.by("date").descending();
	
	@Transactional
	@Override
	public SupplyDto collectSupplyFromText(String text, long organizationId) {
		log.info("Collect supply from text");
		SupplyDto supplyDto = textToSupply.textToSupply(text, organizationService);
		Organization org = organizationService.findById(organizationId);
		Optional<Supply> check = supplyRepository.findBySupplyNumberAndOrganization(supplyDto.getSupplyNumber(),org);
		if (check.isPresent()) {
			if(check.get().getDate().get(Calendar.YEAR)==supplyDto.getDate().get(Calendar.YEAR)) {
				throw new DuplicateItemException("El suministro numero: " + check.get().getSupplyNumber() + "-"
			+ check.get().getDate().get(Calendar.YEAR) +" ya existe.");
			}
		}
		
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
		log.info("Create supply report");
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el suministro"));
		List<SupplyItem> supplyItems = supplyItemRepository.findAllBySupply(supply);
		return getSupplyReport(supplyItems, depositId);
	}

	@Override
	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId, Long depositId) {
		log.info("Create supply correction note");
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
		log.info("private method getSupplyReport");
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
				supplyReportDto.setDepositItemDetail(depositItem.get().getItemDescription());
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
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId) {
		log.info("Find all supplies by organization id");
		Organization organization = organizationService.findById(organizationId);
		List<Supply> supplies = supplyRepository.findAllByOrganization(organization,supplyDatesort);
		return SupplyMapper.INSTANCE.suppliesToDtos(supplies);
	}

	@Override
	public List<SupplyItemDto> findSupplyItems(long supplyId) {
		log.info("Find all supplies items by supply id");
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el suministro"));
		List<SupplyItem> items = supplyItemRepository.findAllBySupply(supply);
		return SupplyItemMapper.INSTANCE.itemToDtos(items);
	}

	@Override
	public int deleteSupply(long supplyId) {
		log.info("Delete supply by id: " + supplyId);
		if (supplyRepository.existsById(supplyId)) {
			int supplyNumberDeleted = supplyRepository.findById(supplyId).get().getSupplyNumber();
			supplyRepository.deleteById(supplyId);
			return supplyNumberDeleted;

		} else {
			throw new ItemNotFoundException("No se encontro el suministro");
		}
	}

	@Override
	public SupplyDto findsupply(long supplyId) {
		log.info("find supply by id: " + supplyId);
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el suministro"));
		return SupplyMapper.INSTANCE.supplyToDto(supply);
	}

	@Override
	public String setOrganizationApplicant(long supplyId,long organizationId) {
		log.info("Set organization Applicant id: " + organizationId);
		Supply supply = supplyRepository.findById(supplyId).orElseThrow(()-> new ItemNotFoundException("No se encontro el suministro"));
		Organization org = organizationService.findById(organizationId);
		supply.setOrganizationApplicant(org);
		supplyRepository.save(supply);
		return org.getOrganizationName();
	}

	@Override
	public List<SupplyItemRequestDto> findAllSupplyItemsByOrganizationApplicant(long organizationApplicantId) {
		log.info("Find all supply items by organization Applicant id: " + organizationApplicantId);
		Organization org = organizationService.findById(organizationApplicantId);
		List<Supply> supplies =supplyRepository.findAllByOrganizationApplicant(org);
		List<SupplyItemRequestDto> itemRequests = supplyItemRepository
				.findAllBySupplyIn(supplies.stream().map(s -> s).toList())
				.stream().map(this::mapSupplyItemsToSupplyItemRequestDto).toList();
		return itemRequests;
	}
	
	private SupplyItemRequestDto mapSupplyItemsToSupplyItemRequestDto(SupplyItem supplyItem){
		if(supplyItem==null) {
			return null;
		}
		SupplyItemRequestDto supplyItemRequestDto = new SupplyItemRequestDto();
		supplyItemRequestDto.setItemId(supplyItem.getId());
		supplyItemRequestDto.setCode(supplyItem.getCode());
		supplyItemRequestDto.setMeasureUnit(supplyItem.getMeasureUnit());
		supplyItemRequestDto.setItemDetail(supplyItem.getItemDetail());
		return supplyItemRequestDto;
	}
	

}
