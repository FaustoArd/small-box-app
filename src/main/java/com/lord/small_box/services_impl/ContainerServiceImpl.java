package com.lord.small_box.services_impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.ContainerRepository;
import com.lord.small_box.services.ContainerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContainerServiceImpl implements ContainerService {
	
	@Autowired
	private final ContainerRepository containerRepository;
	
	private static final String strNotFound = "Container not found";
	
	private static final Calendar now = Calendar.getInstance();

	@Override
	public List<Container> findAll() {
		return (List<Container>) containerRepository.findAll();
	}

	@Override
	public Container save(Container container) {
	container.setSmallBoxDate(now);
	return containerRepository.save(container);
		
	}

	@Override
	public Container findById(Long id) {
		return containerRepository.findById(id).orElseThrow(()-> new ItemNotFoundException(strNotFound));
	}

	@Override
	public void deleteById(Long id) {
	if(containerRepository.existsById(id)) {
		containerRepository.deleteById(id);
	}else {
		throw new ItemNotFoundException(strNotFound);
	}
		
	}

	@Override
	public List<Container> findAllByOrganizations(List<Organization> organizations) {
		return containerRepository.findAllByOrganizationIn(organizations);
	}

	@Override
	public void setContainerTotalWrite(Long containerId, String totalWrite) {
		Container container = containerRepository.findById(containerId).orElseThrow(()-> new ItemNotFoundException(strNotFound));
		container.setTotalWrite(totalWrite);
		containerRepository.save(container);
		
	}
}
