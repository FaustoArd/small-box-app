package com.lord.small_box.services_impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.LocationContractDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.LocationContractMapper;
import com.lord.small_box.models.LocationContract;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.LocationContractRepository;
import com.lord.small_box.services.LocationContractService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationContractServiceImpl implements LocationContractService{

	@Autowired
	private final LocationContractRepository locationContractRepository;
	
	@Autowired
	private final OrganizationService organizationService;
	
	private static final Logger log = LoggerFactory.getLogger(LocationContractServiceImpl.class);

	@Override
	public LocationContract createLocationContract(LocationContractDto locationContractDto) {
log.info("Create location contract");
		LocationContract locationContract = LocationContractMapper.INSTANCE.dtoToLocationContract(locationContractDto);
		locationContract.setHomeDependency("Secretaria de Administracion");
		locationContract.setHomeResponsible("Hernan Sabatella");
		return   locationContractRepository.save(locationContract);
		
	}

	@Override
	public LocationContract findLocationContractById(Long id) {
		log.info("find location contract by id");
		return locationContractRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el contrato de locacion"));
	}

	@Override
	public List<LocationContractDto> findAllLocationContracts() {
		log.info("find all location contract");
		List<LocationContract> locationContracts = locationContractRepository.findAll();
		return LocationContractMapper.INSTANCE.locationContractsToDtos(locationContracts);
	}

	@Override
	public List<LocationContract> findAllLocationContractByOrganizationByUserId(Long userId) {
		log.info("find all location contract by organization and user");
		List<Organization> organizations = organizationService.findAllOrganizationsByUsers(userId);
		List<LocationContract> locationContracts = locationContractRepository.findAllLocationContractsByOrganizationIn(organizations);
		return locationContracts;
	}
}
