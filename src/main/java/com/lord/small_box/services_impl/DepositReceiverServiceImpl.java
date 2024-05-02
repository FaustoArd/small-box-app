package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.DepositReceiverDto;
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
}
