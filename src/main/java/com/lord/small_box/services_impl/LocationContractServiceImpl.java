package com.lord.small_box.services_impl;

import java.util.List;

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

	@Override
	public LocationContract createLocationContract(LocationContractDto locationContractDto) {
		LocationContract locationContract = LocationContractMapper.INSTANCE.dtoToLocationContract(locationContractDto);
		locationContract.setHomeDependency("Secretaria de Administracion");
		locationContract.setHomeResponsible("Hernan Sabatella");
		return   locationContractRepository.save(locationContract);
		
	}

	@Override
	public LocationContract findLocationContractById(Long id) {
		return locationContractRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el contrato de locacion"));
	}

	@Override
	public List<LocationContractDto> findAllLocationContracts() {
		List<LocationContract> locationContracts = locationContractRepository.findAll();
		return LocationContractMapper.INSTANCE.locationContractsToDtos(locationContracts);
	}

	@Override
	public List<LocationContract> findAllLocationContractByOrganizationByUserId(Long userId) {
		List<Organization> organizations = organizationService.findAllOrganizationsByUsers(userId);
		List<LocationContract> locationContracts = locationContractRepository.findAllLocationContractsByOrganizationIn(organizations);
		return locationContracts;
	}
}
