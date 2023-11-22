package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;

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
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SubTotal;
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
	private ContainerService containerService;
	
	private Integer containerId;

	
	@Test
	@Order(1)
	void setup() {
		Calendar now = Calendar.getInstance();
		Container container = Container.builder().smallBoxDate(now)
				.title("Caja chica Super").dependency("Sec. desarr").build();
		Container savedContainer = containerService.save(container); 
		containerId = savedContainer.getId();
		Input input211 = inputService.findById(1);
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2023, 11, 4);
		SmallBox smallBox1 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
				.input(input211).provider("Disalar").ticketTotal(new BigDecimal(5000)).build();
		SmallBox savedSmallBox = smallBoxService.save(smallBox1, savedContainer.getId());
		SmallBox smallBox2 = SmallBox.builder().date(cal1).ticketNumber("2342-12656").input(input211)
				.provider("Almd").ticketTotal(new BigDecimal(9000)).build();
		SmallBox savedSmallBox2 = smallBoxService.save(smallBox2, savedContainer.getId());
		assertEquals(savedSmallBox.getContainer().getDependency(), "Sec. desarr");
		assertEquals(savedSmallBox.getTicketTotal().intValue(), 5000);
		assertEquals(savedSmallBox.getInput().getInputNumber(),"211");

	}
	
	@Test
	@Order(2)
	void calculateSubtotalTest() {
		SubTotal SubTotal = smallBoxService.calculateSubtotal(containerId, "211");
		assertEquals(SubTotal.getSubTotal().intValue(), 14000);
	}
}
