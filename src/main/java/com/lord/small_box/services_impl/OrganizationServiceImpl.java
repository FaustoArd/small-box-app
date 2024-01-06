package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.OrganizationMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{

	
	@Autowired
	private final OrganizationRepository organizationRepository;
	
	@Autowired
	private final AppUserService appUserService;

	@Override
	public List<OrganizationDto> findAll() {
		List<Organization> organizations = organizationRepository.findAll();
		return OrganizationMapper.INSTANCE.toOrganizationsDto(organizations);
	}

	@Override
	public Organization save(Organization organization) {
		return organizationRepository.save(organization);
	}

	@Override
	public Organization findById(Long id) {
		return organizationRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Organization not found"));
	}

	@Override
	public void deleteById(Long id) {
		if(organizationRepository.existsById(id)) {
			organizationRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("Organization Not found");
		}
		
	}

	
	@Override
	public String addOrganizationToUser(Long userId,List<Long> organizationsId) {
		List<Organization> organizations = organizationRepository.findAllById(organizationsId);
		AppUser user = appUserService.findById(userId);
		user.setOrganizations(organizations);
		AppUser updatedUser = appUserService.save(user);
		return "El usuario: " + updatedUser.getName() + updatedUser.getLastname() +
				"Tiene asignada las siguientes dependencias: /n" + updatedUser.getOrganizations().stream().map(o -> o.getOrganizationName() + ",").toString();
	}
	
}
