package com.lord.small_box;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.lord.small_box.models.Input;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.repositories.AuthorityRepository;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.services.AuthorizationService;
import com.lord.small_box.services.ContainerService;
import com.lord.small_box.services.DocumentAIService;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;

@SpringBootApplication
public class SmallBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallBoxApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(InputRepository inputRepository,
			ContainerService containerService,
			SmallBoxService smallBoxService,
			InputService inputService,
			SmallBoxTypeRepository smallBoxTypeRepository,
			OrganizationRepository organizationRepository,
			AuthorityRepository authorityRepository,
			AuthorizationService authorizationService,
			OrganizationResponsibleRepository organizationResponsibleRepository
			) {
		
		return args ->{
			
			Authority admin = new Authority();
			admin.setAuthority(AuthorityName.ADMIN);
			Authority user = new Authority();
			user.setAuthority(AuthorityName.USER);
			Authority superUser = new Authority();
			superUser.setAuthority(AuthorityName.SUPERUSER);
			
			authorityRepository.save(admin);
			authorityRepository.save(user);
			authorityRepository.save(superUser);
			AppUserRegistrationDto userDto = new AppUserRegistrationDto();
			userDto.setName("Carlos");
			userDto.setLastname("Marroin");
			userDto.setEmail("car@mail.com");
			userDto.setUsername("car");
			userDto.setPassword("Pta1919az");
			authorizationService.register(userDto, "ADMIN");
			
			SmallBoxType chica = SmallBoxType.builder()
					.smallBoxType("CHICA").build();
					SmallBoxType savedCHica =  smallBoxTypeRepository.save(chica);
			
			SmallBoxType especial = SmallBoxType.builder().smallBoxType("ESPECIAL")
					.build();
			SmallBoxType savedEspecial = smallBoxTypeRepository.save(especial);
			
			
			
			
		Input i211 = Input.builder().description("Alimento para personas").inputNumber("211").build();
		Input i212 = Input.builder().description("Alimento para animales").inputNumber("212").build();
		Input i213 = Input.builder().description("Productos pecuarios").inputNumber("213").build();
		Input i214 = Input.builder().description("Productos agroforestales").inputNumber("214").build();
		Input i215 = Input.builder().description("Madera ,corcho y sus manufacturas").inputNumber("215").build();
		Input i219 = Input.builder().description("Otros").inputNumber("219").build();
		Input i221 = Input.builder().description("Hilados y Telas").inputNumber("221").build();
		Input i222 = Input.builder().description("Prendas de Vestir").inputNumber("222").build();
		Input i223 = Input.builder().description("Confecciones Textiles").inputNumber("223").build();
		Input i229 = Input.builder().description("Otros").inputNumber("229").build();
		Input i231 = Input.builder().description("Papel de escritorio y carton").inputNumber("231").build();
		Input i232 = Input.builder().description("Papel para computacion").inputNumber("232").build();
		Input i233 = Input.builder().description("Productos de artes graficas").inputNumber("233").build();
		Input i234 = Input.builder().description("Productos de papel y carton").inputNumber("234").build();
		Input i235 = Input.builder().description("Libros, revistas y periodicos").inputNumber("235").build();
		Input i236 = Input.builder().description("Textos de enseñanza").inputNumber("236").build();
		Input i296 = Input.builder().description("Repuestos y accesorios").inputNumber("296").build();
		Input i255 = Input.builder().description("Tintas, pinturas y colorantes").inputNumber("255").build();
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
		inputs.add(i229);
		inputs.add(i231);
		inputs.add(i232);
		inputs.add(i233);
		inputs.add(i234);
		inputs.add(i235);
		inputs.add(i236);
		inputs.add(i296);
		inputs.add(i255);
		inputRepository.saveAll(inputs);
			
		OrganizationResponsible reyes = new OrganizationResponsible();
		reyes.setName("Blasa");
		reyes.setLastname("Reyes");
		OrganizationResponsible savedReyes = organizationResponsibleRepository.save(reyes);
		OrganizationResponsible pierpa = new OrganizationResponsible();
		pierpa.setName("Roxana");
		pierpa.setLastname("Pierpaoli");
		OrganizationResponsible savedPierpa = organizationResponsibleRepository.save(pierpa);
		OrganizationResponsible fabi = new OrganizationResponsible();
		fabi.setName("Fabian");
		fabi.setLastname("Yanes");
		OrganizationResponsible saveFabi = organizationResponsibleRepository.save(fabi);
		OrganizationResponsible fontova = new OrganizationResponsible();
		fontova.setName("Carlos");
		fontova.setLastname("Fontova");
		OrganizationResponsible saveFontova = organizationResponsibleRepository.save(fontova);
		
			
					
		
			Organization org1= new Organization();
			org1.setOrganizationName("Secretaria de desarrollo social");
			org1.setOrganizationNumber(1);
			org1.setMaxRotation(12);
			org1.setMaxAmount(new BigDecimal(180000));
			org1.setResponsible(savedPierpa);
		
			Organization org2 = new Organization();
			org2.setOrganizationName("Direccion de administracion y despacho");
			org2.setOrganizationNumber(2);
			org2.setMaxRotation(12);
			org2.setMaxAmount(new BigDecimal(80000));
			org2.setResponsible(savedReyes);
			
			Organization org3 = new Organization();
			org3.setOrganizationName("Direccion de logistica");
			org3.setOrganizationNumber(3);
			org3.setMaxRotation(12);
			org3.setMaxAmount(new BigDecimal(45000));
			org3.setResponsible(saveFabi);
		
			Organization org4 = new Organization();
			org4.setOrganizationName("Direccion de Niñez");
			org4.setResponsible(saveFontova);
			org4.setMaxRotation(12);
			org4.setMaxAmount(new BigDecimal(45000));
			org4.setOrganizationNumber(4);
		
			Organization secDesSocial = organizationRepository.save(org1);
			Organization dirAdmDesp = organizationRepository.save(org2);
			organizationRepository.save(org3);
			organizationRepository.save(org4);
			
			
		
			Calendar now = Calendar.getInstance();
			Container container = Container.builder().smallBoxDate(now)
					.smallBoxType(savedCHica).organization(dirAdmDesp).responsible(dirAdmDesp.getResponsible()).smallBoxCreated(true).build();
			Container savedContainer = containerService.createContainer(container); 
			
			Input input211 = inputService.findById(1l);
			Input input212 = inputService.findById(2l);
			Input input213 = inputService.findById(3l);
			Input input219 = inputService.findById(6l);
			Calendar cal1 = Calendar.getInstance();
			cal1.set(2023, 11, 4);
			SmallBox smallBox1 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
					.input(input211).provider("Disalar").ticketTotal(new BigDecimal(5000)).build();
			SmallBox savedSmallBox = smallBoxService.save(smallBox1, savedContainer.getId());
			
			SmallBox smallBox8 = SmallBox.builder().date(cal1).ticketNumber("23423-234234")
					.input(input211).provider("Disalar").ticketTotal(new BigDecimal(5000)).build();
			SmallBox savedSmallBox8 = smallBoxService.save(smallBox8, savedContainer.getId());
			
			SmallBox smallBox2 = SmallBox.builder().date(cal1).ticketNumber("2342-12656").input(input211)
					.provider("Papelera rivadavia").ticketTotal(new BigDecimal(9000)).build();
			SmallBox savedSmallBox2 = smallBoxService.save(smallBox2, savedContainer.getId());
			//smallBoxService.calculateSubtotal(container.getId(), "211");
			
			
			SmallBox smallBox3 = SmallBox.builder().date(cal1).ticketNumber("7657-234234")
					.input(input213).provider("Arcor").ticketTotal(new BigDecimal(8000)).build();
			SmallBox savedSmallBox3 = smallBoxService.save(smallBox3, savedContainer.getId());
			
			SmallBox smallBox4 = SmallBox.builder().date(cal1).ticketNumber("987-234234")
					.input(input212).provider("Nikeli").ticketTotal(new BigDecimal(8968)).build();
			SmallBox savedSmallBox4 = smallBoxService.save(smallBox4, savedContainer.getId());
			
			
			SmallBox smallBox5 = SmallBox.builder().date(cal1).ticketNumber("09809-234234")
					.input(input213).provider("Mazloa").ticketTotal(new BigDecimal(8798)).build();
			SmallBox savedSmallBox5 = smallBoxService.save(smallBox5, savedContainer.getId());
			
			SmallBox smallBox6 = SmallBox.builder().date(cal1).ticketNumber("03809-2344234")
					.input(input219).provider("Carlos Boro").ticketTotal(new BigDecimal(5500.20)).build();
			SmallBox savedSmallBox6 = smallBoxService.save(smallBox6, savedContainer.getId());
			
			SmallBox smallBox7 = SmallBox.builder().date(cal1).ticketNumber("03809-23448239")
					.input(input219).provider("Carlos Boro").ticketTotal(new BigDecimal(6700.80)).build();
			SmallBox savedSmallBox7 = smallBoxService.save(smallBox7, savedContainer.getId());
	
		};
	}
}
