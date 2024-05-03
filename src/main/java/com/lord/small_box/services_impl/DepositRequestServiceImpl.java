package com.lord.small_box.services_impl;

import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.DepositControlRequestMapper;
import com.lord.small_box.mappers.DepositControlRequestMapperImpl;
import com.lord.small_box.mappers.DepositRequestMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.DepositControlReceiver;
import com.lord.small_box.models.DepositControlRequest;
import com.lord.small_box.models.DepositReceiver;
import com.lord.small_box.models.DepositRequest;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.DepositControlReceiverRepository;
import com.lord.small_box.repositories.DepositControlRequestRepository;
import com.lord.small_box.repositories.DepositReceiverRepository;
import com.lord.small_box.repositories.DepositRequestRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.DepositRequestService;
import com.lord.small_box.utils.RandomCodeGeneratorUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepositRequestServiceImpl implements DepositRequestService {

	@Autowired
	private DepositRequestRepository depositRequestRepository;

	@Autowired
	private DepositControlRequestRepository depositControlRequestRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private DepositReceiverRepository depositReceiverRepository;

	@Autowired
	private DepositControlReceiverRepository depositControlReceiverRepository;

	@Autowired
	private RandomCodeGeneratorUtil randomCodeGeneratorUtil;

	@Autowired
	private AppUserService appUserService;

	private static final Logger log = LoggerFactory.getLogger(DepositControlRequestMapperImpl.class);

	@Override
	public DepositRequestDto createRequest(DepositRequestDto depositRequestDto) {
		log.info("Destination organization id value:" + depositRequestDto.getDestinationOrganizationId());
		log.info("Create deposit request");
		Organization mainOrganization = organizationRepository.findById(depositRequestDto.getMainOrganizationId())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));
		DepositRequest request = DepositRequestMapper.INSTANCE.dtoToRequest(depositRequestDto);
		request.setMainOrganization(mainOrganization);
		request.setRequestDate(Calendar.getInstance());
		request.setDestinationOrganization(null);
		DepositRequest savedDepositRequest = depositRequestRepository.save(request);
		DepositRequestDto requestDto = DepositRequestMapper.INSTANCE.requestToDto(savedDepositRequest);
		return requestDto;
	}

	@Override
	public DepositRequestDto saveItemsToRequest(DepositRequestDto depositRequestDto) {
		DepositRequest request = depositRequestRepository.findById(depositRequestDto.getId())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el pedido de deposito"));
		List<DepositControlRequest> controlRequests = mapDtoToDepositControlRequest(depositRequestDto, request);
		List<DepositControlRequest> savedControlRequests = depositControlRequestRepository.saveAll(controlRequests);
		List<DepositControlRequestDto> controlRequestDtos= DepositControlRequestMapper.INSTANCE.requestsToDtos(savedControlRequests);
		DepositRequestDto requestDto = DepositRequestMapper.INSTANCE.requestToDto(request);
		requestDto.setDepositControlRequestDtos(controlRequestDtos);
		return requestDto;
	}

	private List<DepositControlRequest> mapDtoToDepositControlRequest(DepositRequestDto depositRequestDto,
			DepositRequest depositRequest) {
		log.info("Map dto to deposit control request");
		List<DepositControlRequest> controlRequests = depositRequestDto.getDepositControlRequestDtos().stream()
				.map(controlDto -> {
					DepositControlRequest controlRequest = new DepositControlRequest();
					controlRequest.setItemCode(controlDto.getItemCode());
					controlRequest.setItemMeasureUnit(controlDto.getItemMeasureUnit());
					controlRequest.setItemQuantity(controlDto.getItemQuantity());
					controlRequest.setItemDescription(controlDto.getItemDescription());
					controlRequest.setDepositRequest(depositRequest);
					return controlRequest;
				}).toList();

		return controlRequests;
		
	}

	@Transactional
	@Override
	public String sendRequest(long depositRequestId, long destinationOrganizationId) {
		log.info("Send deposit request");
		DepositRequest depositRequest = depositRequestRepository.findById(depositRequestId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el pedido de deposito"));

		Organization destinationOrganization = organizationRepository.findById(destinationOrganizationId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));

		DepositReceiver depositReceiver = mapDepositRequestToDepositReceiver(depositRequest, destinationOrganization);
		List<DepositControlRequest> controlRequests = depositControlRequestRepository
				.findAllByDepositRequest(depositRequest);
		mapControlRequestToControlReceiver(controlRequests, depositReceiver);
		String generatedRequestCode = generatedRequestCode(depositRequest.getMainOrganization().getId(), depositRequestId);
		depositReceiver.setDepositRequestCode(generatedRequestCode);
		
		Organization fromOrganization = organizationRepository.findById(depositRequest.getMainOrganization().getId())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));
		
		depositReceiver.setFromOrganization(fromOrganization);
		depositReceiverRepository.save(depositReceiver);
		depositRequest.setRequestCode(generatedRequestCode);
		depositRequest.setDestinationOrganization(destinationOrganization);
		DepositRequest updatedDepositRequest = depositRequestRepository.save(depositRequest);
		return updatedDepositRequest.getRequestCode();
	}

	private String generatedRequestCode(long organizationRequestId, long depositRequestId) {
		log.info("Generate request code");
		return "DS-" + organizationRequestId + "-P-" + depositRequestId
				+ randomCodeGeneratorUtil.generateRandomCode(12, true, true);
	}

	private DepositReceiver mapDepositRequestToDepositReceiver(DepositRequest depositRequest,
			Organization destinationOrganization) {
		log.info("Map deposit request to  deposit receiver");
		DepositReceiver depositReceiver = new DepositReceiver();
		depositReceiver.setOrganization(destinationOrganization);
		depositReceiver.setReceptionDate(Calendar.getInstance());
		return depositReceiver;
	}

	private List<DepositControlReceiver> mapControlRequestToControlReceiver(List<DepositControlRequest> controlRequests,
			DepositReceiver depositReceiver) {
		log.info("Map control request to  control receiver");
		List<DepositControlReceiver> controlReceivers = controlRequests.stream().map(controlRequest -> {
			DepositControlReceiver controlReceiver = new DepositControlReceiver();
			controlReceiver.setItemCode(controlRequest.getItemCode());
			controlReceiver.setItemMeasureUnit(controlRequest.getItemMeasureUnit());
			controlReceiver.setItemQuantity(controlRequest.getItemQuantity());
			controlReceiver.setItemDescription(controlRequest.getItemDescription());
			controlReceiver.setDepositReceiver(depositReceiver);
			return controlReceiver;

		}).toList();
		return depositControlReceiverRepository.saveAll(controlReceivers);
	}

	@Override
	public List<DepositRequestDto> findAllRequestbyOrganizationByUserId(long userId) {
		log.info("Find all request by user organization assigned");
		AppUser user = appUserService.findById(userId);
		List<Organization> orgs = user.getOrganizations().stream().map(org -> org).toList();
		List<DepositRequest> requests = depositRequestRepository.findAllRequestByMainOrganizationIn(orgs);
		return DepositRequestMapper.INSTANCE.requestToDtos(requests);
	}

	@Override
	public List<DepositControlRequestDto> findAllRequestControlsByDepositRequest(long depositRequestId) {
		log.info("Find all control requests by deposit request");
		DepositRequest request = depositRequestRepository.findById(depositRequestId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el pedido de deposito"));
		List<DepositControlRequest> controlRequests = depositControlRequestRepository.findAllByDepositRequest(request);
		return DepositControlRequestMapper.INSTANCE.requestsToDtos(controlRequests);
	}

}
