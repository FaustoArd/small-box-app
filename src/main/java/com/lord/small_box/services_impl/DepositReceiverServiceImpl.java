package com.lord.small_box.services_impl;

import java.lang.invoke.CallSite;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.ControlRequestComparationNoteDto;
import com.lord.small_box.dtos.DepositControlReceiverDto;
import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.dtos.RequestComparationNoteDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.DepositControlReceiverMapper;
import com.lord.small_box.mappers.DepositRecieverMapper;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.DepositControlReceiver;
import com.lord.small_box.models.DepositReceiver;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.DepositControlReceiverRepository;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositReceiverRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.services.DepositRecevierService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositReceiverServiceImpl implements DepositRecevierService {

	@Autowired
	private final DepositReceiverRepository depositReceiverRepository;
	
	@Autowired
	private final DepositControlReceiverRepository depositControlReceiverRepository;
	
	@Autowired
	private final OrganizationService organizationService;
	
	@Autowired
	private final DepositRepository depositRepository;
	
	@Autowired
	private final DepositControlRepository depositControlRepository;
	
	private static final Logger log = LoggerFactory.getLogger(DepositReceiverServiceImpl.class);
	
	private static final Sort controlReceiversDateSort = Sort.by("receptionDate").descending();

	@Override
	public List<DepositReceiverDto> findAllReceiversByOrganization(long organizationId) {
		log.info("Find all receivers by organization, org id: "+ organizationId);
		Organization organization = organizationService.findById(organizationId);
		List<DepositReceiver>  receivers = depositReceiverRepository.findAllByOrganization(organization,controlReceiversDateSort);
		return DepositRecieverMapper.INSTANCE.receiversToDtos(receivers);
	}

	@Override
	public boolean markAsReaded(long depositReceaverId) {
		log.info("Mark receiver as readed, receiver id: "+ depositReceaverId);
		DepositReceiver depositReceiver = depositReceiverRepository.findById(depositReceaverId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la recepcion del pedido de deposito"));
		depositReceiver.setReaded(true);
		DepositReceiver updatedDepositReceiver =  depositReceiverRepository.save(depositReceiver);
		return updatedDepositReceiver.isReaded();
	}

	@Override
	public long countMessages(long organizationId) {
		log.info("Count receivers, org id: "+ organizationId);
		Organization organization = organizationService.findById(organizationId);
		return depositReceiverRepository.findAllByOrganization(organization,controlReceiversDateSort).stream()
				.filter(receiver -> !receiver.isReaded()).count();
	}

	@Override
	public List<DepositControlReceiverDto> findAllByDepositReceiver(long depositReceiverId) {
		log.info("Find all deposit control receivers by depositReceiver id: " + depositReceiverId);
		DepositReceiver receiver = depositReceiverRepository.findById(depositReceiverId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la recepcion de pedido"));
		List<DepositControlReceiver> receivers = depositControlReceiverRepository.findAllByDepositReceiver(receiver);
		return DepositControlReceiverMapper.INSTANCE.receiversToDtos(receivers);
	}

	@Override
	public String deleteDepositReceiver(long depositReceiverId) {
		if(depositReceiverRepository.existsById(depositReceiverId)) {
			String deletedReceiverCode  = findDepositReceiverById(depositReceiverId).getDepositRequestCode();
			depositReceiverRepository.deleteById(depositReceiverId);
			return deletedReceiverCode;
		}
		throw new ItemNotFoundException("No se encontro la recepcion de pedido");
	}
	
	@Override
	public RequestComparationNoteDto createRequestComparationNote(long depositReceiverId,long depositId) {
		log.info("Create request comparation note, receiverId: " + depositReceiverId + ", deposit id: " + depositId);
		DepositReceiver depositReceiver = findDepositReceiverById(depositReceiverId);
		List<DepositControlReceiver> depositControlReceivers = depositControlReceiverRepository
				.findAllByDepositReceiver(depositReceiver);
		Deposit deposit = findDepositById(depositId);
		List<ControlRequestComparationNoteDto> itemComparations = getControlRequestsComparationNote
				(depositControlReceivers, deposit);
		String mainOrganization = organizationService.findById(depositReceiver.getOrganization().getId()).getOrganizationName();
		String fromOrganization = organizationService.findById(depositReceiver.getFromOrganization().getId()).getOrganizationName();
		String depositName = deposit.getName();
		return mapToRequestComparationNote(depositReceiver,depositName, itemComparations, mainOrganization, fromOrganization);
	}
	
	private RequestComparationNoteDto mapToRequestComparationNote(DepositReceiver depositReceiver,String depositName
			,List<ControlRequestComparationNoteDto> controlCompNotes,String mainOrganization,String fromOrganization) {
		RequestComparationNoteDto noteDto = new RequestComparationNoteDto();
		noteDto.setRequestDate(depositReceiver.getReceptionDate());
		noteDto.setRequestCode(depositReceiver.getDepositRequestCode());
		noteDto.setMainOrganization(mainOrganization);
		noteDto.setFromOrganization(fromOrganization);
		noteDto.setItems(controlCompNotes);
		noteDto.setDepositName(depositName);
		return noteDto;
	}
	
	private List<ControlRequestComparationNoteDto> getControlRequestsComparationNote
	(List<DepositControlReceiver> depositControlReceivers,Deposit deposit){
		List<ControlRequestComparationNoteDto> controlComparators = depositControlReceivers.stream().map(receiverItem ->{
			Optional<DepositControl> depositItem = depositControlRepository
					.findByItemCodeAndDeposit(receiverItem.getItemCode(), deposit);
			if(depositItem.isPresent()) {
				return mapItemsToControlRequestComparationNote(receiverItem, depositItem.get());
			}else {
				return mapItemsNotFoundToControlRequestComparatorNote(receiverItem);
			}
		}).toList();
		return controlComparators;
	}
	
	private ControlRequestComparationNoteDto mapItemsNotFoundToControlRequestComparatorNote
	(DepositControlReceiver receiver) {
		ControlRequestComparationNoteDto itemNote = new ControlRequestComparationNoteDto();
		itemNote.setRequestItemCode(receiver.getItemCode());
		itemNote.setRequestItemMeasureUnit(receiver.getItemMeasureUnit());
		itemNote.setRequestItemDescription(receiver.getItemDescription());
		itemNote.setRequestItemQuantity(receiver.getItemQuantity());
		itemNote.setControlItemCode("No encontrado");
		itemNote.setControlItemMeasureUnit("No encontrado");
		itemNote.setControlItemDescription("No encontrado");
		itemNote.setControlItemQuantity(0);
		itemNote.setControlQuantityLeft(0);
		return itemNote;
	}
	
	private ControlRequestComparationNoteDto mapItemsToControlRequestComparationNote
	(DepositControlReceiver receiver,DepositControl control) {
		ControlRequestComparationNoteDto itemNote = new ControlRequestComparationNoteDto();
		itemNote.setRequestItemCode(receiver.getItemCode());
		itemNote.setRequestItemMeasureUnit(receiver.getItemMeasureUnit());
		itemNote.setRequestItemDescription(receiver.getItemDescription());
		itemNote.setRequestItemQuantity(receiver.getItemQuantity());
		itemNote.setControlItemCode(control.getItemCode());
		itemNote.setControlItemMeasureUnit(control.getMeasureUnit());
		itemNote.setControlItemDescription(control.getItemDescription());
		itemNote.setControlItemQuantity(control.getQuantity());
		itemNote.setControlQuantityLeft(control.getQuantity() - receiver.getItemQuantity());
		return itemNote;
	}
	
	private DepositReceiver findDepositReceiverById(long depositReceiverId) {
		log.info("Find receiver by id: " +depositReceiverId);
		return depositReceiverRepository.findById(depositReceiverId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la recepcion de pedido")); 
	}

	
	private Deposit findDepositById(long depositId) {
		return depositRepository.findById(depositId).orElseThrow(()-> new ItemNotFoundException("No se encontro el deposito"));
	}
}
