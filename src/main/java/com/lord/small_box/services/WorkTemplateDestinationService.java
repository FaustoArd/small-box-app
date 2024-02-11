package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.WorkTemplateDestinationDto;


public interface WorkTemplateDestinationService {

	public String createDestination(WorkTemplateDestinationDto destination);
	
	public List<WorkTemplateDestinationDto> findAllDestinations();

	public String deleteDestinationById(Long id);
}
