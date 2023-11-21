package com.lord.small_box.services_impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jdk8.OptionalIntDeserializer;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SubTotal;
import com.lord.small_box.repositories.SmallBoxRepository;
import com.lord.small_box.repositories.SubTotalRepository;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmallBoxServiceImpl implements SmallBoxService {

	@Autowired
	private final SmallBoxRepository smallBoxRepo;
	
	@Autowired
	private final InputService inputService;
	
	@Autowired
	private final SubTotalRepository subTotalRepository;

	

	@Override
	public List<SmallBox> findAll() {
		return (List<SmallBox>) smallBoxRepo.findAll();
	}

	@Override
	public SmallBox findById(Integer id) {
		return smallBoxRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("No se encontro el item"));
	}

	@Override
	public SmallBox save(SmallBox smallBox) {
		Input input = inputService.findById(smallBox.getInput().getId());
		smallBox.setInput(input);
		return smallBoxRepo.save(smallBox);
	}

	@Override
	public void delete(Integer id) {
		if (smallBoxRepo.existsById(id)) {
			smallBoxRepo.deleteById(id);
		} else {
			throw new ItemNotFoundException("No se encontro el item");
		}
	}

	@Override
	public List<SmallBox> findAllOrderByInputInputNumber(String inpuNumber) {
	return (List<SmallBox>)smallBoxRepo.findAllOrderByInputInputNumber(inpuNumber);
	}

	@Override
	public SubTotal calculateSubtotal(List<SmallBox> smallBoxes,Integer inputNumber) {
	 return null;
	}

	@Override
	public List<SmallBox> findAllByContainer(Integer conainerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
