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
			
			WorkTemplateDestination wtdDecDesSocial = WorkTemplateDestination.builder()
					.destination("Secretaria de Desarrollo Social".toUpperCase()).build();
			WorkTemplateDestination wtdCompras = WorkTemplateDestination.builder()
					.destination("Direccion de Compras".toUpperCase()).build();
			WorkTemplateDestination wtdPagos = WorkTemplateDestination.builder()
					.destination("Direccion de Pagos".toUpperCase()).build();
			WorkTemplateDestination wtdSecAdm = WorkTemplateDestination.builder()
					.destination("Secretaria de Administracion".toUpperCase()).build();
			WorkTemplateDestination wtDsPolSocCom = WorkTemplateDestination.builder()
					.destination("Subsecretaria de Politicas Socio Comunitarias".toUpperCase()).build();
			workTemplateDestinationRepository.saveAll(Arrays.asList(wtdDecDesSocial,wtdCompras,wtdPagos,wtdSecAdm,wtDsPolSocCom));
			
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
		Input i256 = Input.builder().description("Combustibles y lubricantes").inputNumber("256").build();
		Input i372 = Input.builder().description("Viaticos").inputNumber("372").build();
		Input i291 = Input.builder().description("Elementos de limpieza").inputNumber("291").build();
		Input i252 = Input.builder().description("Productos farmaceuticos y medicinales").inputNumber("252").build();
		Input i292 = Input.builder().description("Utiles de escritorio, oficina y enseñanza").inputNumber("292").build();
		Input i379 = Input.builder().description("Otros").inputNumber("379").build();
	
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
		inputs.add(i252);
		inputs.add(i255);
		inputs.add(i256);
		inputs.add(i291);
		inputs.add(i296);
		inputs.add(i372);
		inputs.add(i292);
		inputs.add(i379);
		
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
		OrganizationResponsible iasil = new OrganizationResponsible();
		iasil.setName("Luciana");
		iasil.setLastname("Iasil");
		OrganizationResponsible saveIasil = organizationResponsibleRepository.save(iasil);
		
		OrganizationResponsible lagunas = new OrganizationResponsible();
		lagunas.setName("Analia");
		lagunas.setLastname("Lagunas");
		OrganizationResponsible savedLagunas = organizationResponsibleRepository.save(lagunas);
			
					
		
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
			org4.setOrganizationName("Dirección de Reinserción Social");
			org4.setOrganizationNumber(5);
			org4.setMaxAmount(new BigDecimal(100000));
			org4.setMaxRotation(12);
			org4.setResponsible(savedLagunas);
		
			Organization org5 = new Organization();
			org5.setOrganizationName("Subsecretaria de Politicas Socio Comunitarias");
			org5.setResponsible(saveIasil);
			org5.setMaxRotation(12);
			org5.setMaxAmount(new BigDecimal(100000));
			org5.setOrganizationNumber(4);
			
			Organization secDesSocial = organizationRepository.save(org1);
			Organization dirAdmDesp = organizationRepository.save(org2);
			organizationRepository.save(org3);
			organizationRepository.save(org4);
			organizationRepository.save(org5);
			
			
			
		};
	}
}
