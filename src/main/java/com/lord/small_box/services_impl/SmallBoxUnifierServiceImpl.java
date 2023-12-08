package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public SmallBoxUnifier findSmallBoxUnifierbyId(Integer id) {
		return smallBoxUnifierRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el SmallBoxUnifier"));
	}

	@Override
	public SmallBoxUnifier saveSmallBoxUnifier(SmallBoxUnifier smallBoxUnifier) {
		return smallBoxUnifierRepository.save(smallBoxUnifier);
	}

	@Override
	public void deleteSmallBoxUnifierById(Integer id) {
		if(smallBoxUnifierRepository.existsById(id)) {
			smallBoxUnifierRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("No se encontro el SmallBoxUnifier");
		}
		
	}

	@Override
	public List<SmallBoxUnifier> findByContainerId(Integer containerId) {
	return (List<SmallBoxUnifier>) smallBoxUnifierRepository.findByContainerId(containerId);
	}

	

	
}
