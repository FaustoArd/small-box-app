package com.lord.small_box.services_impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger log = LoggerFactory.getLogger(SmallBoxUnifierServiceImpl.class);
	
	@Override
	public List<SmallBoxUnifier> findAllSmallBoxUnifiers() {
		log.info("Find all small box unifiers");
	return (List<SmallBoxUnifier>)smallBoxUnifierRepository.findAll();
	}

	@Override
	public SmallBoxUnifier findSmallBoxUnifierbyId(Long id) {
		log.info("Find  small box unifier by id");
		return smallBoxUnifierRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("No se encontro el SmallBoxUnifier"));
	}

	@Override
	public SmallBoxUnifier saveSmallBoxUnifier(SmallBoxUnifier smallBoxUnifier) {
		log.info("Save  small box unifier");
		return smallBoxUnifierRepository.save(smallBoxUnifier);
	}

	@Override
	public void deleteSmallBoxUnifierById(Long id) {
		log.info("Delete  small box unifier by id");
		if(smallBoxUnifierRepository.existsById(id)) {
			smallBoxUnifierRepository.deleteById(id);
		}else {
			throw new ItemNotFoundException("No se encontro el SmallBoxUnifier");
		}
		
	}

	@Override
	public List<SmallBoxUnifier> findByContainerId(Long containerId) {
		log.info("Find  small box unifier by container id");
	return (List<SmallBoxUnifier>) smallBoxUnifierRepository.findByContainerId(containerId);
	}

	@Transactional
	@Override
	public void deleteAllByContainerId(Long containerId) {
		log.info("delete all  small box unifiers by container id");
		smallBoxUnifierRepository.deleteAllByContainerId(containerId);
		
	}

	

	
}
