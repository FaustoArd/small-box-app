package com.lord.small_box;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;
import com.lord.small_box.models.Container;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.models.WorkTemplateDestination;
import com.lord.small_box.repositories.AuthorityRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.DispatchControlRepository;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;
import com.lord.small_box.repositories.WorkTemplateDestinationRepository;
import com.lord.small_box.services.AuthorizationService;
import com.lord.small_box.services.ContainerService;
import com.lord.small_box.services.DispatchControlService;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;

import com.lord.small_box.services.WorkTemplateDestinationService;
import com.lord.small_box.utils.FileReaderUtils;

import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootApplication
public class SmallBoxApplication {

	private long supplyId;
	
	public static void main(String[] args) {
		SpringApplication.run(SmallBoxApplication.class, args);
	}
	
	//@Bean
	CommandLineRunner run(InputRepository inputRepository,
			ContainerService containerService,
			SmallBoxService smallBoxService,
			InputService inputService,
			SmallBoxTypeRepository smallBoxTypeRepository,
			OrganizationRepository organizationRepository,
			AuthorityRepository authorityRepository,
			AuthorizationService authorizationService,
			OrganizationResponsibleRepository organizationResponsibleRepository,
			FileReaderUtils fileReaderUtils,
			WorkTemplateDestinationRepository workTemplateDestinationRepository,
			PdfToStringUtils pdfToStringUtils,
			SupplyRepository supplyRepository, SupplyItemRepository supplyItemRepository,
			DepositRepository depositRepository) {

		
		return args ->{
			
			Input i252 = Input.builder().description("Utiles de escritorio, oficina y enseñanza").inputNumber("292").build();
			Input i379 = Input.builder().description("Otros").inputNumber("379").build();
			List<Input> inputs = List.of(i252,i379);
			inputRepository.saveAll(inputs);
			
			
			
			
		};
	}
}
