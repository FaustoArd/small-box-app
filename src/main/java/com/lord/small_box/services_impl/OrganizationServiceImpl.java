package com.lord.small_box.services_impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	
	private static final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
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
		
		AppUser user = appUserService.findById(userId);
		log.info("Agregando organizacion al asuario: " + user.getName() + user.getLastname());
		List<Organization> organizations = organizationRepository.findAllById(organizationsId);
			user.setOrganizations(organizations);
			AppUser updatedUser = appUserService.save(user);
			return "El usuario: " + updatedUser.getName()+ " " + updatedUser.getLastname() +
					"Tiene asignada las siguientes dependencias: " + updatedUser.getOrganizations().stream().map(o -> o.getOrganizationName())
					.reduce((org , element) -> org +  ", " + element)
					.orElse( "Ninguna");
		}

	@Override
	public List<OrganizationDto> findOrganizationByUser(Long userId) {
		List<OrganizationDto> orgsDto =  appUserService.findById(userId).getOrganizations().stream().map(org -> {
			OrganizationDto orgDto = new OrganizationDto();
			orgDto.setId(org.getId());
			orgDto.setOrganizationName(org.getOrganizationName());
			orgDto.setOrganizationNumber(org.getOrganizationNumber());
			return orgDto;
		
		}).toList();
		
		return orgsDto;
	}

	@Override
	public List<OrganizationDto> findAllById(List<Long> organizationsId) {
		if(Optional.of(organizationsId).isEmpty()) {
			return null;
		}
	List<Organization> orgs = organizationRepository.findAllById(organizationsId);
	return  OrganizationMapper.INSTANCE.toOrganizationsDto(orgs);
	}
	
}
