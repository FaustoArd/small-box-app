package com.lord.small_box.services_impl;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.ContainerDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.ContainerRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
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

	private static final String containerNotFound = "Container not found";

	private static final Calendar now = Calendar.getInstance();
	
	private static final Logger log = LoggerFactory.getLogger(ContainerServiceImpl.class);

	@Override
	public List<Container> findAll() {
		return (List<Container>) containerRepository.findAll();
	}

	@Override
	public Container save(Container container) {
		log.info("Save container");
		OrganizationResponsible organizationResponsible  = organizationResponsibleRepository
		.findByOrganization(container.getOrganization()).orElseThrow(()-> new ItemNotFoundException("Responsible not found"));
		container.setResponsible(organizationResponsible);
		container.setSmallBoxDate(now);
		return containerRepository.save(container);

	}

	@Override
	public Container findById(Long id) {
		Container container =  containerRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		OrganizationResponsible organizationResponsible  = organizationResponsibleRepository
				.findByOrganization(container.getOrganization()).orElseThrow(()-> new ItemNotFoundException("Responsible not found"));
				container.setResponsible(organizationResponsible);
				return container;
	}

	@Override
	public void deleteById(Long id) {
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
		Container container = containerRepository.findById(containerId)
				.orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		container.setTotalWrite(totalWrite);
		containerRepository.save(container);

	}

	@Override
	public List<ContainerDto> findAllbyOrganizationsByUser(Long userId) {
		log.info("Fetch all containers by user( \"id\" ) assigned organization");
		return containerRepository
				.findAllByOrganizationInOrderByIdAsc(
						appUserService.findById(userId).getOrganizations().stream().map(org -> org).toList())
				.stream().map(container -> {
					ContainerDto containerDto = new ContainerDto();
					containerDto.setId(container.getId());
					containerDto.setOrganization(container.getOrganization().getOrganizationName());
					containerDto.setResponsible(container.getResponsible().getName() + " " +  container.getResponsible().getLastname());
					containerDto.setSmallBoxDate(container.getSmallBoxDate());
					containerDto.setTitle(container.getTitle());
					containerDto.setTotal(container.getTotal());
					containerDto.setTotalWrite(container.getTotalWrite());
					return containerDto;
				}).toList();
	}
}
