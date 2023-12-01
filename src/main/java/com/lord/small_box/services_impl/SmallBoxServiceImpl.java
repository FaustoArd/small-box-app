package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.models.SubTotal;
import com.lord.small_box.repositories.ContainerRepository;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.SmallBoxRepository;
import com.lord.small_box.repositories.SmallBoxUnifierRepository;
import com.lord.small_box.repositories.SubTotalRepository;
import com.lord.small_box.services.SmallBoxService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmallBoxServiceImpl implements SmallBoxService {

	@Autowired
	private final SmallBoxRepository smallBoxRepo;

	@Autowired
	private final InputRepository inputRepository;

	@Autowired
	private final SubTotalRepository subTotalRepository;

	@Autowired
	private final ContainerRepository containerRepository;

	@Autowired
	private final SmallBoxUnifierRepository smallBoxUnifierRepository;

	private String currentInput;

	@Override
	public List<SmallBox> findAll() {
		return (List<SmallBox>) smallBoxRepo.findAll();
	}

	@Override
	public SmallBox findById(Integer id) {
		return smallBoxRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("No se encontro el item"));
	}

	@Override
	public SmallBox save(SmallBox smallBox, Integer containerId) {
		Input input = inputRepository.findById(smallBox.getInput().getId())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el input"));
		Container container = containerRepository.findById(containerId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el container"));
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
	public List<SmallBox> findAllOrderByInputInputNumber(String inputNumber) {
		return (List<SmallBox>) smallBoxRepo.findAllOrderByInputInputNumber(inputNumber);
	}

	@Override
	public SubTotal calculateSubtotal(Integer containerId, String inputNumber) {
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
		return (List<SmallBox>) smallBoxRepo.findAllByContainerIdAndInputInputNumber(containerId, inputNumber);
	}
	

	@Override
	public List<SmallBoxUnifier> completeSmallBox(Integer containerId) {
		List<String> smallBoxes = findAllByContainerIdOrderByInputInputNumber(containerId).stream()
				.map(s -> s.getInput().getInputNumber()).distinct().toList();
		ListIterator<String> smIt = smallBoxes.listIterator();
		while (smIt.hasNext()) {
			findAllByContainerIdAndInputInputNumber(containerId, smIt.next()).forEach(sm -> {
				SmallBoxUnifier smUnifier = new SmallBoxUnifier();
				smUnifier.setDate(sm.getDate());
				smUnifier.setTicketNumber(sm.getTicketNumber());
				smUnifier.setProvider(sm.getProvider());
				smUnifier.setDescription(sm.getInput().getDescription());
				smUnifier.setTicketTotal(sm.getTicketTotal());
				smUnifier.setInputNumber(sm.getInput().getInputNumber());
				currentInput = sm.getInput().getInputNumber();
				smallBoxUnifierRepository.save(smUnifier);
			});
			SmallBoxUnifier smUnifierSTotal = new SmallBoxUnifier();
			smUnifierSTotal.setSubtotal(calculateSubtotal(containerId, currentInput).getSubtotal());
			smUnifierSTotal.setSubtotalTitle("SubTotal");
			smallBoxUnifierRepository.save(smUnifierSTotal);
		}
		return (List<SmallBoxUnifier>) smallBoxUnifierRepository.findAll();

	}

	@Override
	public List<SmallBox> findAllByContainerIdOrderByInputInputNumber(Integer containerId) {
		return (List<SmallBox>) smallBoxRepo.findAllByContainerIdOrderByInputInputNumber(containerId);
	}

}
