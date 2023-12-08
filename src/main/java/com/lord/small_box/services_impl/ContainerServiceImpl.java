package com.lord.small_box.services_impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Container;
import com.lord.small_box.repositories.ContainerRepository;
import com.lord.small_box.services.ContainerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContainerServiceImpl implements ContainerService {
	
	@Autowired
	private final ContainerRepository containerRepository;
	
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
	public Container findById(Integer id) {
		return containerRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el container"));
	}

	@Override
	public void deleteById(Integer id) {
	if(containerRepository.existsById(id)) {
		containerRepository.deleteById(id);
	}else {
		throw new ItemNotFoundException("No se encontro el container");
	}
		
	}
}
