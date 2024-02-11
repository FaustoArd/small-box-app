package com.lord.small_box.services_impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.WorkTemplateDestinationDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.WorkTemplateDestination;
import com.lord.small_box.repositories.WorkTemplateDestinationRepository;
import com.lord.small_box.services.WorkTemplateDestinationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkTemplateDestinationServiceImpl implements WorkTemplateDestinationService {
	
	@Autowired
	private final WorkTemplateDestinationRepository workTemplateDestinationRepository;
	
	private static final Logger log = LoggerFactory.getLogger(WorkTemplateDestinationServiceImpl.class);

	@Override
	public List<WorkTemplateDestinationDto>findAllDestinations() {
		log.info("Find all template destinations");
		return workTemplateDestinationRepository.findAll()
				.stream().map(dest ->{
				WorkTemplateDestinationDto destDto = 	new WorkTemplateDestinationDto();
				destDto.setDestination(dest.getDestination());
				return destDto;
				}).toList();
		
	}

	@Override
	public String createDestination(WorkTemplateDestinationDto workTemplateDestinationDto) {
		log.info("Create new template destination");
		WorkTemplateDestination workTemplateDestination = WorkTemplateDestination.builder()
				.id(workTemplateDestinationDto.getId()).destination(workTemplateDestinationDto.getDestination()).build();
		return workTemplateDestinationRepository.save(workTemplateDestination).getDestination();
	}

	@Override
	public String deleteDestinationById(Long id) {
		log.info("Delete template destination");
		Optional<WorkTemplateDestination> result = workTemplateDestinationRepository.findById(id);
		if(result.isPresent()) {
			workTemplateDestinationRepository.deleteById(id);
			return "Se elimino el destino: " + result.get().getDestination();
		}else {
			throw new ItemNotFoundException("Work template Destination not found");
		}
		
	}
	
	

}
