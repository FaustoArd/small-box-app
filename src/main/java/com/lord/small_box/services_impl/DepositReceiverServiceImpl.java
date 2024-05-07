package com.lord.small_box.services_impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lord.small_box.dtos.DepositControlReceiverDto;
import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.DepositControlReceiverMapper;
import com.lord.small_box.mappers.DepositRecieverMapper;
import com.lord.small_box.models.DepositControlReceiver;
import com.lord.small_box.models.DepositReceiver;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.DepositControlReceiverRepository;
import com.lord.small_box.repositories.DepositReceiverRepository;
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
	
	private static final Logger log = LoggerFactory.getLogger(DepositReceiverServiceImpl.class);
	
	private static final Sort controlReceiversDateSort = Sort.by("receptionDate").descending();

	@Override
	public List<DepositReceiverDto> findAllReceiversByOrganization(long organizationId) {
		Organization organization = organizationService.findById(organizationId);
		List<DepositReceiver>  receivers = depositReceiverRepository.findAllByOrganization(organization,controlReceiversDateSort);
		return DepositRecieverMapper.INSTANCE.receiversToDtos(receivers);
	}

	@Override
	public boolean markAsReaded(long depositReceaverId) {
		DepositReceiver depositReceiver = depositReceiverRepository.findById(depositReceaverId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la recepcion del pedido de deposito"));
		depositReceiver.setReaded(true);
		DepositReceiver updatedDepositReceiver =  depositReceiverRepository.save(depositReceiver);
		return updatedDepositReceiver.isReaded();
	}

	@Override
	public long countMessages(long organizationId) {
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
	
	private DepositReceiver findDepositReceiverById(long depositReceiverId) {
		return depositReceiverRepository.findById(depositReceiverId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la recepcion de pedido")); 
	}

	
}
