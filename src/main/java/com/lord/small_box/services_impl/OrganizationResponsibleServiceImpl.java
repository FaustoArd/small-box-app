package com.lord.small_box.services_impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.OrganizationResponsibleDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.OrganizationResponsibleMapper;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.services.OrganizationResponsibleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationResponsibleServiceImpl implements OrganizationResponsibleService {
	
	@Autowired
	private final OrganizationResponsibleRepository organizationResponsibleRepository;
	
	@Autowired
	private final OrganizationResponsibleMapper organizationResponsibleMapper;
	
	private final Logger log = LoggerFactory.getLogger(OrganizationResponsibleServiceImpl.class);
	
	

	@Override
	public OrganizationResponsibleDto save(OrganizationResponsibleDto organizationResponsibleDto) {
		log.info("Save responsible");
		OrganizationResponsible organizationResponsible = organizationResponsibleMapper.toModel(organizationResponsibleDto);
		OrganizationResponsible savedOrganizationResponsible = organizationResponsibleRepository.save(organizationResponsible);
		return organizationResponsibleMapper.toDto(savedOrganizationResponsible);
	}
	
	@Override
	public OrganizationResponsibleDto update(OrganizationResponsibleDto organizationResponsibleDto) {
		log.info("Update responsible");
		OrganizationResponsible organizationResponsible = organizationResponsibleMapper.toModel(organizationResponsibleDto);
		OrganizationResponsible savedOrganizationResponsible = organizationResponsibleRepository.save(organizationResponsible);
		return organizationResponsibleMapper.toDto(savedOrganizationResponsible);
	}

	@Override
	public OrganizationResponsibleDto findById(Long id) {
		log.info("Find responsible by id");
	OrganizationResponsible organizationResponsible =  organizationResponsibleRepository.findById(id)
			.orElseThrow(()-> new ItemNotFoundException("No se encontro el responsable"));
	return organizationResponsibleMapper.toDto(organizationResponsible);
	}

	@Override
	public List<OrganizationResponsibleDto> findAll() {
		log.info("Find all responsibles");
		List<OrganizationResponsible> responsibles = organizationResponsibleRepository.findAll();
		return organizationResponsibleMapper.toDtoList(responsibles);
	}

	

}
