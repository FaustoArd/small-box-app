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
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
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
	private final OrganizationResponsibleRepository organizationResponsibleRepository;
	
	
	
	@Autowired
	private final AppUserService appUserService;

	@Override
	public List<OrganizationDto> findAll() {
		log.info("Fetching all Organizations");
		List<Organization> organizations = organizationRepository.findAll();
		return OrganizationMapper.INSTANCE.toOrganizationsDto(organizations);
	}

	@Override
	public Organization save(Organization organization) {
		log.info("Save organization");
		OrganizationResponsible responsible = organizationResponsibleRepository.findById(organization.getResponsible().getId())
				.orElseThrow(()-> new ItemNotFoundException("Responsible not found"));
		organization.setResponsible(responsible);
		return organizationRepository.save(organization);
	}

	@Override
	public Organization update(Organization organization) {
		log.info("Update organization");
		OrganizationResponsible responsible = organizationResponsibleRepository.findById(organization.getResponsible().getId())
				.orElseThrow(()-> new ItemNotFoundException("Responsible not found"));
		organization.setResponsible(responsible);
		return organizationRepository.save(organization);
		
	}

	@Override
	public Organization findById(Long id) {
		log.info("Find organization by id");
		return organizationRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Organization not found"));
	}

	@Override
	public void deleteById(Long id) {
		log.info("Deleting organization");
		if(organizationRepository.existsById(id)) {
			organizationRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("Organization Not found");
		}
		
	}

	@Override
	public String addOrganizationToUser(Long userId,List<Long> organizationsId) {
		log.info("Assigning organization to user");
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
		log.info("Fetching organization by user ID");
		List<OrganizationDto> orgsDto =  appUserService.findById(userId).getOrganizations().stream().map(org -> {
			OrganizationDto orgDto = new OrganizationDto();
			orgDto.setId(org.getId());
			orgDto.setOrganizationName(org.getOrganizationName());
			orgDto.setOrganizationNumber(org.getOrganizationNumber());
			orgDto.setMaxRotation(org.getMaxRotation());
			orgDto.setMaxAmount(org.getMaxAmount());
			return orgDto;
		
		}).toList();
		
		return orgsDto;
	}

	@Override
	public List<OrganizationDto> findAllById(List<Long> organizationsId) {
		log.info("Fetching all organizations by organizations ID");
		if(Optional.of(organizationsId).isEmpty()) {
			return null;
		}
	List<Organization> orgs = organizationRepository.findAllById(organizationsId);
	return  OrganizationMapper.INSTANCE.toOrganizationsDto(orgs);
	}

	
}
