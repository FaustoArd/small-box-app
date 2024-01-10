package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.OrganizationResponsibleDto;
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
	public OrganizationResponsibleDto findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrganizationResponsibleDto> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
