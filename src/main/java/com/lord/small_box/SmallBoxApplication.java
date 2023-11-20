package com.lord.small_box;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.lord.small_box.models.Input;

import com.lord.small_box.repositories.InputRepository;

@SpringBootApplication
public class SmallBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallBoxApplication.class, args);
	}

	@Bean
	CommandLineRunner run(InputRepository inputRepository) {
		return args ->{
			
		//List<Input> inputs = inputRepository.findAll();
		//List<Input> result = inputs.stream().filter(f -> f.getInputNumber()<250).map(m -> m).collect(Collectors.toList());
		//result.forEach(e -> System.out.println(e));
		};
	}
}
