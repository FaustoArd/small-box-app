package com.lord.small_box.services_impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.dtos.ParentOrganizationDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.OrganizationMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.ParentOrganization;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.ParentOrganizationRepository;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.OrganizationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

	private static final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

	@Autowired
	private final OrganizationRepository organizationRepository;

	@Autowired
	private final OrganizationResponsibleRepository organizationResponsibleRepository;

	@Autowired
	private final AppUserService appUserService;

	@Autowired
	private final ParentOrganizationRepository parentOrganizationRepository;

	@Override
	public List<OrganizationDto> findAll() {
		log.info("Fetching all Organizations");
		List<Organization> organizations = organizationRepository.findAll();
		return OrganizationMapper.INSTANCE.toOrganizationsDto(organizations);
	}

	@Override
	public Organization save(Organization organization) {

		log.info("Save organization");
		OrganizationResponsible responsible = findOrganizationResposibleById(organization.getResponsible().getId());
		organization.setResponsible(responsible);
		return organizationRepository.save(organization);
	}

	@Override
	public Organization update(Organization organization) {
		log.info("Update organization");
		OrganizationResponsible responsible = findOrganizationResposibleById(organization.getResponsible().getId());
		organization.setResponsible(responsible);
		return organizationRepository.save(organization);

	}

	@Override
	public Organization findById(Long id) {
		log.info("Find organization by id");
		return findOrganizationById(id);
	}

	@Override
	public void deleteById(Long id) {
		log.info("Deleting organization");
		if (organizationRepository.existsById(id)) {
			organizationRepository.deleteById(id);
		} else {
			throw new ItemNotFoundException("Organization Not found");
		}

	}

	@Override
	public String addOrganizationToUser(Long userId, List<Long> organizationsId) {
		log.info("Add organization to user");
		AppUser user = appUserService.findById(userId);
		log.info("Assigning organization to user: " + user.getName() + user.getLastname());
		List<Organization> organizations = organizationRepository.findAllById(organizationsId);
		user.setOrganizations(organizations);
		AppUser updatedUser = appUserService.save(user);
		return "El usuario: " + updatedUser.getName() + " " + updatedUser.getLastname()
				+ " Tiene asignada las siguientes dependencias: "
				+ updatedUser.getOrganizations().stream().map(o -> o.getOrganizationName())
						.reduce((org, element) -> org + ", " + element).orElse("Ninguna");
	}

	@Override
	public List<OrganizationDto> findAllById(List<Long> organizationsId) {
		log.info("Fetching all organizations by organizations ID");
		if (Optional.of(organizationsId).isEmpty()) {
			return null;
		}

		List<Organization> orgs = organizationRepository.findAllById(organizationsId);
		return OrganizationMapper.INSTANCE.toOrganizationsDto(orgs);
	}

	@Override
	public List<Organization> findAllOrganizationsByUsers(Long userId) {
		log.info("Find all organizations by user id");
		AppUser findedUser = appUserService.findById(userId);
		List<Organization> orgs = organizationRepository.findAllOrganizationsByUsers(findedUser);
		return orgs;
	}

	@Override
	public long setUserMainOrganization(long organizationId, long userId) {
		log.info("Set user main organization." + "user id:" + userId + " organization id:" + organizationId);
		AppUser user = appUserService.findById(userId);
		user.setMainOrganizationId(organizationId);
		AppUser savedUser = appUserService.save(user);
		return savedUser.getMainOrganizationId();
	}

	@Override
	public long getUserMainOrganization(long userId) {
		log.info("Get user main organization");
		AppUser user = appUserService.findById(userId);
		if (user.getMainOrganizationId() < 1L) {
			log.info("Main organization not found, returning 0");
			return 0;
		} else {
			log.info("Main organization found. id: " + user.getMainOrganizationId());
			return user.getMainOrganizationId();
		}
	}

	/*@Override
	public List<String> addOganizationReceiversToUser(long userId, List<Long> organizationReceiverIds) {
		List<Organization> organizations = organizationRepository.findAllById(organizationReceiverIds);
		AppUser user = appUserService.findById(userId);
		user.setOrganizationReceivers(organizations);
		appUserService.save(user);

		return organizations.stream().map(org -> {
			return org.getOrganizationName();
		}).toList();
	}*/

	/*@Override
	public List<OrganizationDto> findAllDestinationOrganizations(long userId) {
		AppUser user = appUserService.findById(userId);
		List<Long> organizationIds = user.getOrganizationReceivers().stream().map(org -> org.getId()).toList();
		List<Organization> organizations = organizationRepository.findAllById(organizationIds);
		return OrganizationMapper.INSTANCE.toOrganizationsDto(organizations);
	}*/

	@Override
	public ParentOrganizationDto setParentOrganization(ParentOrganizationDto parentOrganizationDto) {
		Organization mainOrganization = findOrganizationById(parentOrganizationDto.getMainOrganizationId());
		List<Organization> parentOrganizations = organizationRepository
				.findAllById(parentOrganizationDto.getParentOrganizationIds());
		ParentOrganization savedParentOrganization = parentOrganizationRepository
				.save(mapToParentOrganization(parentOrganizationDto,mainOrganization, parentOrganizations));
		List<String> parentOrganizationNames =  organizationRepository
				.findAllById(savedParentOrganization.getParentOrganizations().stream().map(p -> p.getId()).toList())
				.stream().map(org -> org.getOrganizationName()).toList();
		return mapParentOrganizationToDto(savedParentOrganization, mainOrganization,parentOrganizationNames);
	}

	private ParentOrganization mapToParentOrganization(ParentOrganizationDto parentOrganizationDto,Organization mainOrganization,
			List<Organization> parentOrganizations) {
		ParentOrganization parentOrganization = new ParentOrganization();
		parentOrganization.setId(parentOrganizationDto.getId());
		parentOrganization.setMainOrganization(mainOrganization);
		parentOrganization.setParentOrganizations(parentOrganizations);
		return parentOrganization;
	}
	
	private ParentOrganizationDto mapParentOrganizationToDto(ParentOrganization parentOrganization,Organization mainOrganization,
			List<String> parentOrganizationNames) {
		ParentOrganizationDto  parentOrganizationDto = new ParentOrganizationDto();
		parentOrganizationDto.setId(parentOrganization.getId());
		parentOrganizationDto.setMainOrganizationId(parentOrganization.getMainOrganization().getId());
		parentOrganizationDto.setMainOrganizationName(mainOrganization.getOrganizationName());
		parentOrganizationDto.setParentOrganizationIds
		(parentOrganization.getParentOrganizations().stream().map(m -> m.getId()).toList());
		parentOrganizationDto.setParentOrganizationNames(parentOrganizationNames);
		return parentOrganizationDto;
	}

	@Override
	public List<OrganizationDto> findParentOrganizationsByMainOrganization(long mainOrganizationId) {
		log.info("find parent organizations by main organization id: " + mainOrganizationId);
		Organization mainOrganization = findOrganizationById(mainOrganizationId);
		Optional<ParentOrganization> parent = parentOrganizationRepository.findByMainOrganization(mainOrganization);
		if (parent.isPresent()) {
			List<Organization> organizations = organizationRepository
					.findAllById(parent.get().getParentOrganizations().stream().map(p -> p.getId()).toList());
			return OrganizationMapper.INSTANCE.toOrganizationsDto(organizations);
		}
		throw new ItemNotFoundException("No se encontro la organizacion padre");
	}
	
	
	

	@Override
	public ParentOrganizationDto findParentOrganizationByMainOrganizationId(long mainOrganizationId) {
		Organization mainOrganization = findOrganizationById(mainOrganizationId);
		ParentOrganization parentOrganization=  findParentOrganizationByMainOrganization(mainOrganization);
		List<String>  parentOrganizationNames = organizationRepository
				.findAllById(parentOrganization.getParentOrganizations().stream().map(org -> org.getId()).toList())
				.stream().map(parentOrg -> parentOrg.getOrganizationName()).toList();
		return mapParentOrganizationToDto(parentOrganization, mainOrganization, parentOrganizationNames);
	}
	private Organization findOrganizationById(long organizationId) {
		return organizationRepository.findById(organizationId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));
	}
	private OrganizationResponsible findOrganizationResposibleById(long organizationResponsibleId) {
		return organizationResponsibleRepository.findById(organizationResponsibleId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro al responsable de la organizacion"));
	}
	private ParentOrganization findParentOrganizationByMainOrganization(Organization mainOrganization) {
		return parentOrganizationRepository.findByMainOrganization(mainOrganization).orElseThrow(()-> new ItemNotFoundException("No se encontro la organizacion padre"));
	}

}
