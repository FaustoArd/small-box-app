package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.repositories.SmallBoxUnifierRepository;
import com.lord.small_box.services.SmallBoxUnifierService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmallBoxUnifierServiceImpl implements SmallBoxUnifierService {
	
	@Autowired
	private final SmallBoxUnifierRepository smallBoxUnifierRepository;
	
	@Override
	public List<SmallBoxUnifier> findAllSmallBoxUnifiers() {
	return (List<SmallBoxUnifier>)smallBoxUnifierRepository.findAll();
	}

	@Override
	public SmallBoxUnifier findSmallBoxUnifierbyId(Long id) {
		return smallBoxUnifierRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el SmallBoxUnifier"));
	}

	@Override
	public SmallBoxUnifier saveSmallBoxUnifier(SmallBoxUnifier smallBoxUnifier) {
		return smallBoxUnifierRepository.save(smallBoxUnifier);
	}

	@Override
	public void deleteSmallBoxUnifierById(Long id) {
		if(smallBoxUnifierRepository.existsById(id)) {
			smallBoxUnifierRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("No se encontro el SmallBoxUnifier");
		}
		
	}

	@Override
	public List<SmallBoxUnifier> findByContainerId(Long containerId) {
	return (List<SmallBoxUnifier>) smallBoxUnifierRepository.findByContainerId(containerId);
	}

	@Transactional
	@Override
	public void deleteAllByContainerId(Long containerId) {
		smallBoxUnifierRepository.deleteAllByContainerId(containerId);
		
	}

	

	
}
