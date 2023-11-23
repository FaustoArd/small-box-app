package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jdk8.OptionalIntDeserializer;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SubTotal;
import com.lord.small_box.repositories.ContainerRepository;
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
	
	@Autowired
	private final ContainerRepository containerRepository;

	

	@Override
	public List<SmallBox> findAll() {
		return (List<SmallBox>) smallBoxRepo.findAll();
	}

	@Override
	public SmallBox findById(Integer id) {
		return smallBoxRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("No se encontro el item"));
	}

	@Override
	public SmallBox save(SmallBox smallBox,Integer containerId) {
		Input input = inputService.findById(smallBox.getInput().getId());
		Container container = containerRepository.findById(containerId).orElseThrow(()-> new ItemNotFoundException("No se encontro el container"));
		smallBox.setInput(input);
		smallBox.setContainer(container);
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
	public SubTotal calculateSubtotal(Integer containerId,String inputNumber) {
	List<SmallBox> smallBoxes = smallBoxRepo.findAllByContainerIdAndInputInputNumber(containerId, inputNumber);
	Double totalResult = smallBoxes.stream().mapToDouble(s -> s.getTicketTotal().doubleValue()).sum();
	SubTotal subTotal = SubTotal.builder().subtotal(new BigDecimal(totalResult)).build();
	SubTotal savedSubtotal = subTotalRepository.save(subTotal);
	smallBoxes.forEach(s -> s.setSubtotal(savedSubtotal));
	smallBoxRepo.saveAll(smallBoxes);
	return savedSubtotal;
	}

	@Override
	public List<SmallBox> findAllByContainerId(Integer containerId) {
	return (List<SmallBox>) smallBoxRepo.findAllByContainerId(containerId);
	}

	@Override
	public List<SmallBox> findAllByContainerIdAndInputInputNumber(Integer containerId, String inputNumber) {
		return (List<SmallBox>)smallBoxRepo.findAllByContainerIdAndInputInputNumber(containerId, inputNumber);
	}

	@Override
	public SmallBox insertSubtotalInColumn(Integer containerId) {
		List<SmallBox> smallBoxes = findAllByContainerId(containerId);
		ListIterator<SmallBox> smallBoxesIt =smallBoxes.listIterator();
		return null;
	}

}
