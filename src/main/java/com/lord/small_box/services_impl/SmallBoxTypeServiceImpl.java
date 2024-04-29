package com.lord.small_box.services_impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger log = LoggerFactory.getLogger(SmallBoxServiceImpl.class);
	
	public SmallBoxTypeServiceImpl(SmallBoxTypeRepository smallBoxTypeRepository) {
		this.smallBoxTypeRepository = smallBoxTypeRepository;	
	}
	
	@Override
	public SmallBoxType findById(Long id) {
		log.info("Find small box type by id");
		return smallBoxTypeRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("SmallBoxType not found"));
	}

	@Override
	public SmallBoxType save(SmallBoxType smallBoxType) {
		log.info("save small box type");
		return smallBoxTypeRepository.save(smallBoxType);
	}

	@Override
	public List<SmallBoxType> findAll() {
		log.info("Find all small box type");
		return (List<SmallBoxType>)smallBoxTypeRepository.findAll();
	}

	@Override
	public void delete(Long id) {
		log.info("delete small box type");
		// TODO Auto-generated method stub
		
	}

}
