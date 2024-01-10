package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.models.Container;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.services.ContainerService;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;
import com.lord.small_box.services.SmallBoxUnifierService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CompleteSmallBoxTest {
	
	@Autowired
	private SmallBoxService smallBoxService;
	
	@Autowired
	private InputService inputService;
	
	@Autowired
	private InputRepository inputRepository;
	
	@Autowired
	private SmallBoxUnifierService smallBoxUnifierService;
	
	@Autowired
	private OrganizationResponsibleRepository organizationResponsibleRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private SmallBoxTypeRepository smallBoxTypeRepository;
	
	@Autowired
	private ContainerService containerService;
	
	private Long containerId;
	
	@Test
	@Order(1)
	void setup() {
		Calendar now = Calendar.getInstance();
		
		Input i211 = Input.builder().description("Alimento para personas").inputNumber("211").build();
		Input i212 = Input.builder().description("Alimento para animales").inputNumber("212").build();
		Input i213 = Input.builder().description("Productos pecuarios").inputNumber("213").build();
		Input i214 = Input.builder().description("Productos agroforestales").inputNumber("214").build();
		Input i215 = Input.builder().description("Madera ,corcho y sus manufacturas").inputNumber("215").build();
		Input i219 = Input.builder().description("Otros").inputNumber("219").build();
		Input i221 = Input.builder().description("Hilados y Telas").inputNumber("221").build();
		Input i222 = Input.builder().description("Prendas de Vestir").inputNumber("222").build();
		Input i223 = Input.builder().description("Confecciones Textiles").inputNumber("223").build();
		List<Input> inputs = new ArrayList<>();
		inputs.add(i211);
		inputs.add(i212);
		inputs.add(i213);
		inputs.add(i214);
		inputs.add(i215);
		inputs.add(i219);
		inputs.add(i221);
		inputs.add(i222);
		inputs.add(i223);
		inputRepository.saveAll(inputs);
		
		OrganizationResponsible pierpa = new OrganizationResponsible();
		pierpa.setName("Roxana");
		pierpa.setLastname("Pierpaoli");
		OrganizationResponsible savedPierpa = organizationResponsibleRepository.save(pierpa);
		
		Organization secDesSocial = new Organization();
		secDesSocial.setOrganizationName("Secretaria de Desarrollo Social");
		secDesSocial.setOrganizationNumber(1);
		secDesSocial.setResponsible(savedPierpa);
		Organization savedSecDesSocial = organizationRepository.save(secDesSocial);
			
		SmallBoxType chica = SmallBoxType.builder()
				.smallBoxType("CHICA")
				.maxRotation(12)
				.maxAmount(new BigDecimal(45000)).build();
		SmallBoxType savedCHica =  smallBoxTypeRepository.save(chica);
		
		SmallBoxType especial = SmallBoxType.builder()
				.maxRotation(3)
				.maxAmount(new BigDecimal(200000)).build();
		SmallBoxType savedEspecial = smallBoxTypeRepository.save(especial);
		
		Container container = Container.builder().smallBoxDate(now)
				.smallBoxType(savedCHica).organization(savedSecDesSocial).responsible(savedSecDesSocial.getResponsible()).build();
		Container savedContainer = containerService.save(container); 
		containerId = savedContainer.getId();
		Input input211 = inputService.findById(1l);
		Input input212 = inputService.findById(2l);
		Input input213 = inputService.findById(3l);
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2023, 11, 4);
		
		//Input 211
		SmallBox smallBox1 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
				.input(input211).provider("Disalar").ticketTotal(new BigDecimal(5000)).build();
		SmallBox savedSmallBox = smallBoxService.save(smallBox1, savedContainer.getId());
		
		//Input 211
		SmallBox smallBox2 = SmallBox.builder().date(cal1).ticketNumber("2342-12656").input(input211)
				.provider("Almd").ticketTotal(new BigDecimal(9000)).build();
		SmallBox savedSmallBox2 = smallBoxService.save(smallBox2, savedContainer.getId());
		
		//Input 212
				SmallBox smallBox5 = SmallBox.builder().date(cal1).ticketNumber("987-234234")
						.input(input212).provider("Item 4 Nikecli").ticketTotal(new BigDecimal(3250.20)).build();
				SmallBox savedSmallBox5 = smallBoxService.save(smallBox5, savedContainer.getId());
		
		//Input 213
		SmallBox smallBox3 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
				.input(input213).provider("Item 1 Disalar").ticketTotal(new BigDecimal(5000)).build();
		SmallBox savedSmallBox3 = smallBoxService.save(smallBox3, savedContainer.getId());
		
		//Input 212
		SmallBox smallBox4 = SmallBox.builder().date(cal1).ticketNumber("987-234234")
				.input(input212).provider("Item 4 Nikecli").ticketTotal(new BigDecimal(8968)).build();
		SmallBox savedSmallBox4 = smallBoxService.save(smallBox4, savedContainer.getId());
		
		
		
		assertEquals(savedSmallBox.getTicketTotal().intValue(), 5000);
		assertEquals(savedSmallBox.getInput().getInputNumber(),"211");

	}
	
	@Test
	@Order(2)
	void completeSmallBox() {
		
		List<SmallBoxUnifier> smUnified = smallBoxService.completeSmallBox(containerId);
		
		assertEquals(smUnified.get(2).getSubtotal().intValue(), 14000);
		assertEquals(smUnified.get(5).getSubtotal().doubleValue(), 12218.20);
		assertEquals(smUnified.get(7).getSubtotal().intValue(), 5000);
	}
	
	@Test
	@Order(3)
	void addAllTicketTotals() {
		smallBoxService.addAllTicketTotals(containerId);
		Container container = containerService.findById(containerId);
		assertEquals(container.getTotal().doubleValue(), 31218.20);
	}
	
	@Test
	@Order(4)
	void deleteAllByContainerId() {
		smallBoxUnifierService.deleteAllByContainerId(containerId);
		List<SmallBoxUnifier> smUnifiers = smallBoxUnifierService.findByContainerId(containerId);
		assertEquals(smUnifiers.size(), 0);
	}

}
