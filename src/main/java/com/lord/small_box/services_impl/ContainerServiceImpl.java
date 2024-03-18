package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.exceptions.MaxRotationExceededException;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.repositories.ContainerRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.services.AppUserService;
import com.lord.small_box.services.ContainerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContainerServiceImpl implements ContainerService {

	@Autowired
	private final ContainerRepository containerRepository;

	@Autowired
	private final OrganizationResponsibleRepository organizationResponsibleRepository;

	@Autowired
	private final AppUserService appUserService;

	@Autowired
	private SmallBoxTypeRepository smallBoxTypeRepository;

	@Autowired
	private final OrganizationRepository organizationRepository;

	private static final String containerNotFound = "Container not found";

	private static final Calendar now = Calendar.getInstance();

	private static final Logger log = LoggerFactory.getLogger(ContainerServiceImpl.class);

	@Override
	public List<Container> findAll() {
		return (List<Container>) containerRepository.findAll();
	}

	@Transactional
	@Override
	public Container createContainer(Container container) throws MaxRotationExceededException {
		log.info("Save container");
		SmallBoxType smallBoxType = smallBoxTypeRepository
				.findBySmallBoxType(container.getSmallBoxType().getSmallBoxType())
				.orElseThrow(() -> new ItemNotFoundException("SmallBoxType not found"));
		
		Organization organization = organizationRepository.findById(container.getOrganization().getId())
				.orElseThrow(() -> new ItemNotFoundException("Organization not found"));
		
		if (smallBoxType.getSmallBoxType().equals("CHICA")) {
			if (organization.getCurrentRotation() + 1 > organization.getMaxRotation()) {
				throw new MaxRotationExceededException("Ya se supero la rotacion maxima de rendiciones de caja chica");
			}
			organization.setCurrentRotation(organization.getCurrentRotation() + 1);
			
		}
		Organization savedOrganization =  organizationRepository.save(organization);
		OrganizationResponsible organizationResponsible = organizationResponsibleRepository
				.findById(organization.getResponsible().getId())
				.orElseThrow(() -> new ItemNotFoundException("Responsible not found"));
		
		container.setResponsible(organizationResponsible);
		container.setOrganization(savedOrganization);
		container.setSmallBoxDate(now);
		container.setSmallBoxType(smallBoxType);
		return containerRepository.save(container);

	}
	
	@Override
	public Container update(Container container) {
		log.info("Update container");
		Container updatedContainer = containerRepository.findById(container.getId())
				.orElseThrow(()-> new ItemNotFoundException(containerNotFound));
		Organization organization = organizationRepository.findById(container.getOrganization().getId())
				.orElseThrow(() -> new ItemNotFoundException("Organization not found"));
		SmallBoxType smallBoxType = smallBoxTypeRepository
				.findBySmallBoxType(container.getSmallBoxType().getSmallBoxType())
				.orElseThrow(() -> new ItemNotFoundException("SmallBoxType not found"));
		OrganizationResponsible organizationResponsible = organizationResponsibleRepository
				.findById(organization.getResponsible().getId())
				.orElseThrow(() -> new ItemNotFoundException("Responsible not found"));
		updatedContainer.setResponsible(organizationResponsible);
		updatedContainer.setOrganization(organization);
		updatedContainer.setSmallBoxDate(now);
		updatedContainer.setSmallBoxType(smallBoxType);
		return containerRepository.save(updatedContainer);
	}

	@Override
	public Container findById(Long id) {
		log.info("Find container by id");
		Container container = containerRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		OrganizationResponsible organizationResponsible = organizationResponsibleRepository
				.findById(container.getResponsible().getId())
				.orElseThrow(() -> new ItemNotFoundException("Responsible not found"));
		container.setResponsible(organizationResponsible);
		return container;
	}

	@Override
	public void deleteById(Long id) {
		log.info("Delete container by id");
		if (containerRepository.existsById(id)) {
			containerRepository.deleteById(id);
		} else {
			throw new ItemNotFoundException(containerNotFound);
		}

	}

	@Override
	public List<Container> findAllByOrganizations(List<Organization> organizations) {
		log.info("Fetch all containers by organizations");
		return containerRepository.findAllByOrganizationInOrderByIdAsc(organizations);
	}

	@Override
	public void setContainerTotalWrite(Long containerId, String totalWrite) {
		log.info("Setting container total write");
		Container container = containerRepository.findById(containerId)
				.orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		container.setTotalWrite(totalWrite);
		containerRepository.save(container);

	}

	@Override
	public List<ContainerDto> findAllbyOrganizationsByUser(Long userId) {
		log.info("Fetch all containers by user assigned organization");
		return containerRepository
				.findAllByOrganizationInOrderByIdAsc(
						appUserService.findById(userId).getOrganizations().stream().map(org -> org).toList())
				.stream().map(container -> {
					ContainerDto containerDto = new ContainerDto();
					containerDto.setId(container.getId());
					containerDto.setOrganization(container.getOrganization().getOrganizationName());
					containerDto.setResponsible(
							container.getResponsible().getName() + " " + container.getResponsible().getLastname());
					containerDto.setSmallBoxDate(container.getSmallBoxDate());
					containerDto.setSmallBoxType(container.getSmallBoxType().getSmallBoxType());
					containerDto.setTotal(container.getTotal());
					containerDto.setTotalWrite(container.getTotalWrite());
					return containerDto;
				}).toList();
	}

	@Override
	public BigDecimal getSmallBoxMaxAmount(Long containerId) {
		long orgId = containerRepository.findById(containerId)
				.map(m -> m.getOrganization()
						.getId())
				.orElseThrow(()-> new ItemNotFoundException("No se encontro el container"));
		return organizationRepository.findById(orgId).map(m -> m.getMaxAmount()).get();
	}

	
}
