package com.lord.small_box.services_impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.exceptions.MaxAmountExeededException;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.models.SubTotal;
import com.lord.small_box.repositories.ContainerRepository;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.SmallBoxRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.repositories.SmallBoxUnifierRepository;
import com.lord.small_box.repositories.SubTotalRepository;
import com.lord.small_box.services.OrganizationService;
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
	
	@Autowired
	private final OrganizationRepository organizationRepository;
	
	@Autowired
	private final SmallBoxTypeRepository smallBoxTypeRepository;
	
	private static final String containerNotFound = "No se encontro el container";
	
	private static final Logger log = LoggerFactory.getLogger(SmallBoxServiceImpl.class);

	private String currentInput;

	@Override
	public List<SmallBox> findAll() {
		log.info("Fetch all SmallBoxes");
		return (List<SmallBox>) smallBoxRepo.findAll();
	}

	@Override
	public SmallBox findById(Long id) {
		log.info("Fetch SmallBox by id");
		return smallBoxRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("No se encontro el item"));
	}

	@Override
	public SmallBox save(SmallBox smallBox, Long containerId) {
		log.info("Saving smallbox");
		Input input = inputRepository.findById(smallBox.getInput().getId())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el input"));
		Container container = containerRepository.findById(containerId)
				.orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		smallBox.setInput(input);
		smallBox.setContainer(container);
		return smallBoxRepo.save(smallBox);
	}
	
	@Override
	public SmallBox update(SmallBox smallBox) {
		log.info("Updating smallbox");
		Input input = inputRepository.findByDescription(smallBox.getInput().getDescription())
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el input"));
		Container container = containerRepository.findById(smallBox.getContainer().getId())
				.orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		smallBox.setInput(input);
		smallBox.setContainer(container);
		return smallBoxRepo.save(smallBox);
	}
	
	

	@Override
	public void delete(Long id) {
		log.info("Deleting smallbox");
		if (smallBoxRepo.existsById(id)) {
			smallBoxRepo.deleteById(id);
		} else {
			throw new ItemNotFoundException("No se encontro el item");
		}
	}

	@Override
	public List<SmallBox> findAllOrderByInputInputNumber(String inputNumber) {
		log.info("Fetch All smallBox by input number");
		return (List<SmallBox>) smallBoxRepo.findAllOrderByInputInputNumber(inputNumber);
	}

	@Override
	public SubTotal calculateSubtotal(Long containerId, String inputNumber) {
		log.info("Calculate smallBoxes subtotal");
		List<SmallBox> smallBoxes = smallBoxRepo.findAllByContainerIdAndInputInputNumber(containerId, inputNumber);
		Double totalResult = smallBoxes.stream().mapToDouble(s -> s.getTicketTotal().doubleValue()).sum();
		SubTotal subTotal = SubTotal.builder().subtotal(new BigDecimal(totalResult)).build();
		SubTotal savedSubtotal = subTotalRepository.save(subTotal);
		smallBoxes.forEach(s -> s.setSubtotal(savedSubtotal));
		smallBoxRepo.saveAll(smallBoxes);
		return savedSubtotal;
	}

	@Override
	public List<SmallBox> findAllByContainerId(Long containerId) {
		log.info("Fetch al smallBoxes by container id");
		return (List<SmallBox>) smallBoxRepo.findAllByContainerId(containerId);
	}

	@Override
	public List<SmallBox> findAllByContainerIdAndInputInputNumber(Long containerId, String inputNumber) {
		log.info("Fetch al smallBoxes by container and input number");
		return (List<SmallBox>) smallBoxRepo.findAllByContainerIdAndInputInputNumber(containerId, inputNumber);
	}
	/**This method take charge of  sort by input number,calculate all the tickets subtotals and add title to each subtotal. **/
	@Transactional
	@Override
	public List<SmallBoxUnifier> completeSmallBox(Long containerId) {
		log.info("Complete small box");
		//find container
		Container container = containerRepository.findById(containerId).orElseThrow(() -> new ItemNotFoundException(containerNotFound));
		//find all SmallBoxes by container and map all the input numbers, return a List string.
		List<String> smallBoxes = findAllByContainerIdOrderByInputInputNumber(containerId).stream()
				.map(s -> s.getInput().getInputNumber()).distinct().toList();
		//Create an input numbers list iterator
		ListIterator<String> smIt = smallBoxes.listIterator();
		
		//while input numbers has next, find all SmallBox by container id and input number, and
		//iterate with a forEach, then , inside the for each, create a new SmallBoxUnified,
		//copy the data from the forEach consumer and then save SmallBoxUnified to database
		while (smIt.hasNext()) {
			findAllByContainerIdAndInputInputNumber(containerId, smIt.next()).forEach(sm -> {
				
			currentInput = 	mapSmallBoxToSmallBoxUnifier(sm, container);
			});
			//then create a new SmallBoxUnifier to set the sub total of all  the tickets that correspond
			//to the input number.
			setSmallBoxUnifierSubTotal(containerId, currentInput, container);
		}
		addAllTicketTotals(containerId);
		return (List<SmallBoxUnifier>) smallBoxUnifierRepository.findByContainerId(containerId);

	}
	private String mapSmallBoxToSmallBoxUnifier(SmallBox sm,Container container) {
		SmallBoxUnifier smUnifier = new SmallBoxUnifier();
		smUnifier.setDate(sm.getDate());
		smUnifier.setTicketNumber(sm.getTicketNumber());
		smUnifier.setProvider(sm.getProvider());
		smUnifier.setDescription(sm.getInput().getDescription());
		smUnifier.setTicketTotal(sm.getTicketTotal());
		smUnifier.setInputNumber(sm.getInput().getInputNumber());
		smUnifier.setContainer(container);
		smallBoxUnifierRepository.save(smUnifier);
		return sm.getInput().getInputNumber();
		
	}
	
	private void setSmallBoxUnifierSubTotal(long containerId,String currentInput,Container container) {
		SmallBoxUnifier smUnifierSTotal = new SmallBoxUnifier();
		smUnifierSTotal.setSubtotal(calculateSubtotal(containerId, currentInput).getSubtotal());
		smUnifierSTotal.setSubtotalTitle("SubTotal");
		smUnifierSTotal.setContainer(container);
		container.setSmallBoxCreated(true);
		containerRepository.save(container);
		smallBoxUnifierRepository.save(smUnifierSTotal);
	}
	
	
	
	@Transactional
	@Override
	public void addAllTicketTotals(Long containerId) {
		log.info("Add total $ amount to container");
		Double totalResult =  findAllByContainerId(containerId).stream().mapToDouble(sm -> sm.getTicketTotal().doubleValue()).sum();
		Container container= containerRepository.findById(containerId).orElseThrow(()-> new ItemNotFoundException(containerNotFound));
		container.setTotal(new BigDecimal(totalResult));
		 containerRepository.save(container);
		
		
	}
	
	@Override
	public List<SmallBox> findAllByContainerIdOrderByInputInputNumber(Long containerId) {
		log.info("Fetch all smallBoxes by container id order by input number");
		return (List<SmallBox>) smallBoxRepo.findAllByContainerIdOrderByInputInputNumber(containerId);
	}

	@Override
	public String checkMaxAmount(Long containerId)throws MaxAmountExeededException {
		log.info("Check Total max amount");
		Container container = containerRepository.findById(containerId).orElseThrow(()-> new ItemNotFoundException(containerNotFound));
		SmallBoxType smType = smallBoxTypeRepository.findById(container.getSmallBoxType().getId())
				.orElseThrow(()-> new ItemNotFoundException("No se encontro el tipo de rendicion"));
		if(smType.getSmallBoxType().equals("CHICA")) {
			double result = findAllByContainerId(containerId).stream().mapToDouble(sm -> sm.getTicketTotal().doubleValue()).sum();
			Organization organization = organizationRepository.findById(container.getOrganization().getId())
					.orElseThrow(()-> new ItemNotFoundException("Organization Not found"));
			if(result>organization.getMaxAmount().doubleValue()) {
				throw new MaxAmountExeededException("El total de la rendicion exede el maximo permitido de: $" + organization.getMaxAmount());
			}
			return "Total permitido";
		}
		
		return "Total permitido";
	}

	



}
