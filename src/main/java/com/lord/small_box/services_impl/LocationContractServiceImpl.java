package com.lord.small_box.services_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.LocationContractDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.LocationContractMapper;
import com.lord.small_box.models.LocationContract;
import com.lord.small_box.repositories.LocationContractRepository;
import com.lord.small_box.services.LocationContractService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationContractServiceImpl implements LocationContractService{

	@Autowired
	private final LocationContractRepository locationContractRepository;

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
}
