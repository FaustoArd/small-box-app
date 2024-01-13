package com.lord.small_box.services_impl;

import java.util.List;

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
	
	

	@Override
	public OrganizationResponsibleDto save(OrganizationResponsibleDto organizationResponsibleDto) {
		OrganizationResponsible organizationResponsible = organizationResponsibleMapper.toModel(organizationResponsibleDto);
		OrganizationResponsible savedOrganizationResponsible = organizationResponsibleRepository.save(organizationResponsible);
		return organizationResponsibleMapper.toDto(savedOrganizationResponsible);
	}
	
	@Override
	public OrganizationResponsibleDto update(OrganizationResponsibleDto organizationResponsibleDto) {
		OrganizationResponsible organizationResponsible = organizationResponsibleMapper.toModel(organizationResponsibleDto);
		OrganizationResponsible savedOrganizationResponsible = organizationResponsibleRepository.save(organizationResponsible);
		return organizationResponsibleMapper.toDto(savedOrganizationResponsible);
	}

	@Override
	public OrganizationResponsibleDto findById(Long id) {
	OrganizationResponsible organizationResponsible =  organizationResponsibleRepository.findById(id)
			.orElseThrow(()-> new ItemNotFoundException("No se encontro el responsable"));
	return organizationResponsibleMapper.toDto(organizationResponsible);
	}

	@Override
	public List<OrganizationResponsibleDto> findAll() {
		List<OrganizationResponsible> responsibles = organizationResponsibleRepository.findAll();
		return organizationResponsibleMapper.toDtoList(responsibles);
	}

	

}
