package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.LocationContractDto;
import com.lord.small_box.models.LocationContract;

public interface LocationContractService {

	public LocationContract createLocationContract(LocationContractDto locationContractDto);
	
	public LocationContract findLocationContractById(Long id);
	
	public List<LocationContractDto> findAllLocationContracts();
}
