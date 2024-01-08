package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.models.Container;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SubTotal;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.services.ContainerService;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalculateSubTotalTest {
	
	@Autowired
	private SmallBoxService smallBoxService;
	
	@Autowired
	private InputService inputService;
	
	@Autowired
	private InputRepository inputRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private OrganizationResponsibleRepository organizationResponsibleRepository;
	
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
			
		
		Container container = Container.builder().smallBoxDate(now)
				.title("Caja chica Super").organization(savedSecDesSocial).responsible(savedSecDesSocial.getResponsible()).build();
		Container savedContainer = containerService.save(container); 
		containerId = savedContainer.getId();
		Input input211 = inputService.findById(1l);
		Input input212 = inputService.findById(2l);
		Input input213 = inputService.findById(3l);
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2023, 11, 4);
		SmallBox smallBox1 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
				.input(input211).provider("Disalar").ticketTotal(new BigDecimal(5000)).build();
		
		SmallBox savedSmallBox = smallBoxService.save(smallBox1, savedContainer.getId());
		
		SmallBox smallBox2 = SmallBox.builder().date(cal1).ticketNumber("2342-12656").input(input211)
				.provider("Almd").ticketTotal(new BigDecimal(9000)).build();
		
		SmallBox savedSmallBox2 = smallBoxService.save(smallBox2, savedContainer.getId());
		
		SmallBox smallBox3 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
				.input(input213).provider("Item 1 Disalar").ticketTotal(new BigDecimal(5000)).build();
		SmallBox savedSmallBox3 = smallBoxService.save(smallBox3, savedContainer.getId());
		
		SmallBox smallBox4 = SmallBox.builder().date(cal1).ticketNumber("987-234234")
				.input(input212).provider("Item 4 Nikecli").ticketTotal(new BigDecimal(8968)).build();
		SmallBox savedSmallBox4 = smallBoxService.save(smallBox4, savedContainer.getId());
		
		
		
		assertEquals(savedSmallBox.getTicketTotal().intValue(), 5000);
		assertEquals(savedSmallBox.getInput().getInputNumber(),"211");

	}
	
	@Test
	@Order(2)
	void calculateSubtotalTest() {
		SubTotal SubTotal = smallBoxService.calculateSubtotal(containerId, "211");
		assertEquals(SubTotal.getSubtotal().intValue(), 14000);
	}
	
}
