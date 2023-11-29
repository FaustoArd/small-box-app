package com.lord.small_box;

import java.math.BigDecimal;
import java.util.Calendar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.services.ContainerService;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;

@SpringBootApplication
public class SmallBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallBoxApplication.class, args);
	}

	@Bean
	CommandLineRunner run(InputRepository inputRepository,ContainerService containerService,SmallBoxService smallBoxService,
			InputService inputService) {
		return args ->{
			
		//List<Input> inputs = inputRepository.findAll();
		//List<Input> result = inputs.stream().filter(f -> f.getInputNumber()<250).map(m -> m).collect(Collectors.toList());
		//result.forEach(e -> System.out.println(e));
			
			Calendar now = Calendar.getInstance();
			Container container = Container.builder().smallBoxDate(now)
					.title("Caja chica Super").dependency("Sec. desarr").build();
			Container savedContainer = containerService.save(container); 
			
			Input input211 = inputService.findById(1);
			Input input212 = inputService.findById(2);
			Input input213 = inputService.findById(3);
			Calendar cal1 = Calendar.getInstance();
			cal1.set(2023, 11, 4);
			SmallBox smallBox1 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
					.input(input213).provider("Item 1 Disalar").ticketTotal(new BigDecimal(5000)).build();
			SmallBox savedSmallBox = smallBoxService.save(smallBox1, savedContainer.getId());
			
			SmallBox smallBox2 = SmallBox.builder().date(cal1).ticketNumber("2342-12656").input(input211)
					.provider("Item 2 Almd").ticketTotal(new BigDecimal(9000)).build();
			SmallBox savedSmallBox2 = smallBoxService.save(smallBox2, savedContainer.getId());
			//smallBoxService.calculateSubtotal(container.getId(), "211");
			
			
			SmallBox smallBox3 = SmallBox.builder().date(cal1).ticketNumber("7657-234234")
					.input(input213).provider("Item 3 JOGe").ticketTotal(new BigDecimal(8000)).build();
			SmallBox savedSmallBox3 = smallBoxService.save(smallBox3, savedContainer.getId());
			
			SmallBox smallBox4 = SmallBox.builder().date(cal1).ticketNumber("987-234234")
					.input(input212).provider("Item 4 Nikecli").ticketTotal(new BigDecimal(8968)).build();
			SmallBox savedSmallBox4 = smallBoxService.save(smallBox4, savedContainer.getId());
			
			
			SmallBox smallBox5 = SmallBox.builder().date(cal1).ticketNumber("09809-234234")
					.input(input213).provider("Item 5 Mazloa").ticketTotal(new BigDecimal(8798)).build();
			SmallBox savedSmallBox5 = smallBoxService.save(smallBox5, savedContainer.getId());
		
		};
	}
}
