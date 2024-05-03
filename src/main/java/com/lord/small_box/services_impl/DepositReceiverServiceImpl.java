package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.DepositReceiverDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.DepositRecieverMapper;
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

	@Override
	public List<DepositReceiverDto> findAllReceiversByOrganization(long organizationId) {
		Organization organization = organizationService.findById(organizationId);
		List<DepositReceiver>  receivers = depositReceiverRepository.findAllByOrganization(organization);
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
		return depositReceiverRepository.findAllByOrganization(organization).stream()
				.filter(receiver -> !receiver.isReaded()).count();
	}
}
