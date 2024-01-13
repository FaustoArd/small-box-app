package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.services.SmallBoxTypeService;

@Service
public class SmallBoxTypeServiceImpl implements SmallBoxTypeService {

	@Autowired
	private final SmallBoxTypeRepository smallBoxTypeRepository;
	
	public SmallBoxTypeServiceImpl(SmallBoxTypeRepository smallBoxTypeRepository) {
	this.smallBoxTypeRepository = smallBoxTypeRepository;	
	}
	
	@Override
	public SmallBoxType findById(Long id) {
		return smallBoxTypeRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("SmallBoxType not found"));
	}

	@Override
	public SmallBoxType save(SmallBoxType smallBoxType) {
		return smallBoxTypeRepository.save(smallBoxType);
	}

	@Override
	public List<SmallBoxType> findAll() {
		return (List<SmallBoxType>)smallBoxTypeRepository.findAll();
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
