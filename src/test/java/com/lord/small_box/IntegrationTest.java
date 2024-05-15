package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.stream;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import com.google.gson.Gson;
import com.lord.small_box.controllers.SupplyController;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.dtos.DepositControlRequestDto;
import com.lord.small_box.dtos.DepositRequestDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.dtos.OrganizationDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.BigBagItem;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.DepositControlReceiver;
import com.lord.small_box.models.DepositControlRequest;
import com.lord.small_box.models.DepositReceiver;
import com.lord.small_box.models.ExcelItemContainer;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.models.Supply;
import com.lord.small_box.repositories.AppUserRepository;
import com.lord.small_box.repositories.AuthorityRepository;
import com.lord.small_box.repositories.DepositControlReceiverRepository;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositControlRequestRepository;
import com.lord.small_box.repositories.DepositReceiverRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.ExcelItemContainerRepository;
import com.lord.small_box.repositories.ExcelItemRepository;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;
import com.lord.small_box.services.AuthorizationService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTest {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private SmallBoxTypeRepository smallBoxTypeRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseOrderItemRepository purchaseOrderItemRepository;
	
	@Autowired
	private SupplyRepository supplyRepository;
	
	@Autowired
	private SupplyItemRepository supplyItemRepository;

	@Autowired
	private InputRepository inputRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private DepositRepository depositRepository;

	@Autowired
	private DepositControlRepository depositControlRepository;
	
	@Autowired
	private DepositReceiverRepository depositReceiverRepository;
	
	@Autowired
	private DepositControlReceiverRepository depositControlReceiverRepository;

	@Autowired
	private ExcelItemRepository excelItemRepository;

	@Autowired
	private ExcelItemContainerRepository excelItemContainerRepository;

	@Autowired
	private MockMvc mockMvc;

	private MvcResult mvcResult;

	private String jwtToken;

	private String userPedrojwtToken;

	private String superUserMiguel248JwtToken;

	private long userPedro29Id;

	private Long lagunasId;

	private Long yanezId;

	private long hemerdingerId;

	private Long sitCalleId;

	private String container1Id;

	private String smallBoxRow3Id;

	private String adminCarPassword;

	private Organization oldSitDeCalleOrg;

	private AppUser oldUserPedro29;

	@BeforeAll
	void setup() {
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
		userDto.setPassword("Dvf8650123");
		authorizationService.register(userDto, "ADMIN");

		SmallBoxType chica = SmallBoxType.builder().smallBoxType("CHICA").build();
		SmallBoxType savedCHica = smallBoxTypeRepository.save(chica);

		SmallBoxType especial = SmallBoxType.builder().smallBoxType("ESPECIAL").build();
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
	}

	@Test
	@Order(1)
	void loginAsAdmin() throws Exception {

		mvcResult = mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/authorization/login")
						.content("{\"username\":\"car\",\"password\":\"Dvf8650123\"}")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.userId", is(notNullValue()))).andExpect(jsonPath("$.token", is(notNullValue())))
				.andReturn();

		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		for (String str : list) {
			if (str.length() > 200) {
				jwtToken = str;
			}
		}

	}

	@Test
	@Order(2)
	void registerUserPedro29() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/registration/register").content(
				"{\"name\":\"Pedro\",\"lastname\":\"Mozart\",\"username\":\"pedro29\",\"email\":\"car_moz@gmail.com\",\"password\":\"Xta2929341\"}")
				.header("Authorization", "Bearer " + jwtToken).param("authority", "USER")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath(("$.name"), is("Pedro"))).andExpect(jsonPath("$.password", is(nullValue())));

	}

	@Test
	@Order(3)
	void createResponsible() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-responsible")
						.content("{\"name\":\"Analia\",\"lastname\":\"Lagunas\"}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is("Analia")))
				.andExpect(jsonPath("$.lastname", is("Lagunas"))).andReturn();
		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		String s = Character.toString(list[2].charAt(1));
		lagunasId = Long.parseLong(s);

	}

	@Test
	@Order(4)
	void createAnotherResponsible() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-responsible")
						.content("{\"name\":\"Carlos\",\"lastname\":\"Heimerdinger\"}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is("Carlos")))
				.andExpect(jsonPath("$.lastname", is("Heimerdinger"))).andReturn();
		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		String s = Character.toString(list[2].charAt(1));
		hemerdingerId = Long.parseLong(s);

	}

	@Test
	@Order(5)
	void createOrganization() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-organization").content(
						"{\"organizationName\":\"Dir de personas en situacion de calle\",\"organizationNumber\":1,\"responsibleId\":1,\"maxRotation\":12,\"maxAmount\":45000}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Dir de personas en situacion de calle")))
				.andExpect(jsonPath("$.maxRotation", is(12))).andExpect(jsonPath("$.maxAmount", is(45000)))
				.andExpect(jsonPath("$.responsible", is("Analia Lagunas"))).andReturn();
		oldSitDeCalleOrg = organizationRepository.findById(1L)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));
	}

	@Test
	@Order(6)
	void createNewResponsible() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-responsible")
						.content("{\"name\":\"Fabian\",\"lastname\":\"Yanez\"}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is("Fabian")))
				.andExpect(jsonPath("$.lastname", is("Yanez"))).andReturn();
		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		String s = Character.toString(list[2].charAt(1));
		yanezId = Long.parseLong(s);
	}

	@Test
	@Order(7)
	void createNewOrganization() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-organization").content(
						"{\"organizationName\":\"Dir  de Logistica\",\"organizationNumber\":2,\"responsibleId\":3,\"maxRotation\":12,\"maxAmount\":45000}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Dir  de Logistica")))
				.andExpect(jsonPath("$.maxRotation", is(12))).andExpect(jsonPath("$.maxAmount", is(45000)))
				.andExpect(jsonPath("$.responsible", is("Fabian Yanez"))).andReturn();
	}

	@Test
	@Order(8)
	void addOrganizationToUserPedro() throws Exception {

		mvcResult = this.mockMvc
				.perform(put("http://localhost:8080/api/v1/smallbox/organization/add-organization").param("userId", "2")
						.param("organizationsId", "1").header("Authorization", "Bearer " + jwtToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
		String stringResult = mvcResult.getResponse().getContentAsString();
		boolean doesContain = stringResult
				.contains("El usuario: Pedro Mozart Tiene asignada las siguientes dependencias:"
						+ " Dir de personas en situacion de calle");
		assertTrue(doesContain);

	}

	@Test
	@Order(9)
	void findAllOrganizationsByUser() throws Exception {

		mvcResult = this.mockMvc
				.perform(get("http://localhost:8080/api/v1/smallbox/organization/all-orgs-by-user").param("userId", "2")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$[0].id", is(notNullValue()))).andExpect(jsonPath("$[0].id", is(not(0))))
				.andExpect(jsonPath("$[0].organizationName", is("Dir de personas en situacion de calle")))
				.andExpect(jsonPath("$[0].responsible", is("Analia Lagunas")))
				.andExpect(jsonPath("$[0].responsibleId", is(notNullValue())))
				.andExpect(jsonPath("$[0].responsibleId", is(not(0))))
				.andExpect(jsonPath("$[0].currentRotation", is(0))).andExpect(jsonPath("$[0].maxRotation", is(12)))
				.andExpect(jsonPath("$[0].maxAmount", is(45000.0))).andReturn();

	}

	@Test
	@Order(10)
	void loginAsUserPedro() throws Exception {
		mvcResult = mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/authorization/login")
						.content("{\"username\":\"pedro29\",\"password\":\"Xta2929341\"}")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.userId", is(notNullValue()))).andExpect(jsonPath("$.token", is(notNullValue())))
				.andReturn();

		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		for (String str : list) {
			if (str.length() > 200) {
				userPedrojwtToken = str;
			}
		}
	}

	@Test
	@Order(11)
	void whenTryToCreateResponsibleWithUserPedroMustReturn403Forbidden() throws Exception {
		this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-responsible")
						.content("{\"name\":\"Alfonso\",\"lastname\":\"Gomez\"}")
						.header("Authorization", "Bearer " + userPedrojwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(403)).andReturn();
	}

	@Test
	@Order(12)
	void whenTryToCreateOrganizationWithUserPedroMustReturn403Forbidden() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-organization").content(
				"{\"organizationName\":\"Dir. de Presuspuesto\",\"organizationNumber\":14,\"responsibleId\":1,\"maxRotation\":12,\"maxAmount\":45000}")
				.header("Authorization", "Bearer " + userPedrojwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(403));

	}

	@Test
	@Order(13)
	void whenTryToRegisterUserWithUserPedroMustReturn403Forbidden() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/registration/register").content(
				"{\"name\":\"Mariano\",\"lastname\":\"Pergamino\",\"username\":\"marper\",\"email\":\"mar@gmail.com\",\"password\":\"123\"}")
				.header("Authorization", "Bearer " + userPedrojwtToken).param("authority", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(403));

	}

	@Test
	@Order(14)
	void createContainerWithUserPedro() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/containers/")
						.content("{\"smallBoxType\":\"CHICA\",\"organizationId\":1}")
						.header("Authorization", "Bearer " + userPedrojwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201)).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.id", is(not(0))))
				.andExpect(jsonPath("$.smallBoxType", is("CHICA")))
				.andExpect(jsonPath("$.organization", is("Dir de personas en situacion de calle")))
				.andExpect(jsonPath("$.responsible", is("Analia Lagunas"))).andReturn();

		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		container1Id = Character.toString(list[2].charAt(1));

	}

	@Test
	@Order(15)
	void createSmallBoxRow1WithUserPedro() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/smallboxes/new").content(
				"{\"date\":\"2023-02-10\",\"ticketNumber\":\"0001-2423\",\"provider\":\"Disalar\",\"inputId\":2,\"ticketTotal\":4000}")
				.param("containerId", container1Id).header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.id", is(not(0))))
				.andExpect(jsonPath("$.date", is("2023-02-10"))).andExpect(jsonPath("$.ticketNumber", is("0001-2423")))
				.andExpect(jsonPath("$.provider", is("Disalar")))
				.andExpect(jsonPath("$.description", is("Alimento para animales")))
				.andExpect(jsonPath("$.ticketTotal", is(4000)))
				.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));

	}

	@Test
	@Order(16)
	void createSmallBoxRow2WithUserPedro() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/smallboxes/new").content(
				"{\"date\":\"2023-04-15\",\"ticketNumber\":\"0002-2223\",\"provider\":\"La Roma\",\"inputId\":1,\"ticketTotal\":3000}")
				.param("containerId", container1Id).header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.id", is(not(0))))
				.andExpect(jsonPath("$.date", is("2023-04-15"))).andExpect(jsonPath("$.ticketNumber", is("0002-2223")))
				.andExpect(jsonPath("$.provider", is("La Roma")))
				.andExpect(jsonPath("$.description", is("Alimento para personas")))
				.andExpect(jsonPath("$.ticketTotal", is(3000)))
				.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));

	}

	@Test
	@Order(17)
	void createSmallBoxRow3WithUserPedro() throws Exception {
		mvcResult = this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/smallboxes/new").content(
				"{\"date\":\"2024-05-11\",\"ticketNumber\":\"00001-23223\",\"provider\":\"Bengala\",\"inputId\":2,\"ticketTotal\":2500.50}")
				.param("containerId", container1Id).header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.id", is(not(0)))).andExpect(jsonPath("$.date", is("2024-05-11")))
				.andExpect(jsonPath("$.ticketNumber", is("00001-23223")))
				.andExpect(jsonPath("$.provider", is("Bengala")))
				.andExpect(jsonPath("$.description", is("Alimento para animales")))
				.andExpect(jsonPath("$.ticketTotal", is(2500.50)))
				.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id)))).andReturn();

		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		smallBoxRow3Id = Character.toString(list[2].charAt(1));
		System.err.println(smallBoxRow3Id);

	}

	@Test
	@Order(18)
	void createSmallBoxRow4WithUserPedro() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/smallboxes/new").content(
				"{\"date\":\"2024-05-10\",\"ticketNumber\":\"00001-25223\",\"provider\":\"La Comarca S.R.L\",\"inputId\":6,\"ticketTotal\":3000.50}")
				.param("containerId", container1Id).header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.id", is(not(0))))
				.andExpect(jsonPath("$.date", is("2024-05-10")))
				.andExpect(jsonPath("$.ticketNumber", is("00001-25223")))
				.andExpect(jsonPath("$.provider", is("La Comarca S.R.L")))
				.andExpect(jsonPath("$.description", is("Otros"))).andExpect(jsonPath("$.ticketTotal", is(3000.50)))
				.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));

	}

	@Test
	@Order(19)
	void createSmallBoxRow5WithUserPedro() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/smallboxes/new").content(
				"{\"date\":\"2022-05-10\",\"ticketNumber\":\"00001-25228\",\"provider\":\"Alimentos Carlos\",\"inputId\":1,\"ticketTotal\":6000}")
				.param("containerId", container1Id).header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.id", is(not(0))))
				.andExpect(jsonPath("$.date", is("2022-05-10")))
				.andExpect(jsonPath("$.ticketNumber", is("00001-25228")))
				.andExpect(jsonPath("$.provider", is("Alimentos Carlos")))
				.andExpect(jsonPath("$.description", is("Alimento para personas")))
				.andExpect(jsonPath("$.ticketTotal", is(6000)))
				.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));
	}

	@Test
	@Order(20)
	void editSmallBoxRow3WithUserPedro() throws Exception {
		mvcResult = this.mockMvc
				.perform(put("http://localhost:8080/api/v1/smallbox/smallboxes/smallBox-update").content(
						"{\"id\":3,\"date\":\"2024-05-11\",\"ticketNumber\":\"00001-23226\",\"provider\":\"Bengala\",\"containerId\":1,\"description\":\"Alimento para animales\",\"ticketTotal\":2500.50}")
						.header("Authorization", "Bearer " + userPedrojwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andDo(MockMvcResultHandlers.print()).andReturn();

		String stringResult = mvcResult.getResponse().getContentAsString();
		boolean doesContain = stringResult.contains("Se actualizo el comprobante Numero: " + "00001-23226");
		assertTrue(doesContain);
	}

	@Test
	@Order(21)
	void completeSmallBoxWithUserPedro() throws Exception {
		mvcResult = this.mockMvc
				.perform(put("http://localhost:8080/api/v1/smallbox/smallboxes/complete")
						.param("containerId", container1Id).header("Authorization", "Bearer " + userPedrojwtToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$[0].id", is(not(0)))).andExpect(jsonPath("$[0].id", is(notNullValue())))
				.andExpect(jsonPath("$[0].date", is("2023-04-15")))
				.andExpect(jsonPath("$[0].ticketNumber", is("0002-2223")))
				.andExpect(jsonPath("$[0].provider", is("La Roma"))).andExpect(jsonPath("$[0].inputNumber", is("211")))
				.andExpect(jsonPath("$[0].ticketTotal", is(3000.0))).andExpect(jsonPath("$[1].id", is(not(0))))
				.andExpect(jsonPath("$[1].id", is(notNullValue()))).andExpect(jsonPath("$[1].date", is("2022-05-10")))
				.andExpect(jsonPath("$[1].ticketNumber", is("00001-25228")))
				.andExpect(jsonPath("$[1].provider", is("Alimentos Carlos")))
				.andExpect(jsonPath("$[1].inputNumber", is("211"))).andExpect(jsonPath("$[1].ticketTotal", is(6000.0)))
				.andExpect(jsonPath("$[2].id", is(not(0)))).andExpect(jsonPath("$[2].id", is(notNullValue())))
				.andExpect(jsonPath("$[2].subtotal", is(9000)))
				.andExpect(jsonPath("$[2].subtotalTitle", is("SubTotal"))).andExpect(jsonPath("$[3].id", is(not(0))))
				.andExpect(jsonPath("$[3].id", is(notNullValue()))).andExpect(jsonPath("$[3].date", is("2023-02-10")))
				.andExpect(jsonPath("$[3].ticketNumber", is("0001-2423")))
				.andExpect(jsonPath("$[3].provider", is("Disalar"))).andExpect(jsonPath("$[3].inputNumber", is("212")))
				.andExpect(jsonPath("$[3].ticketTotal", is(4000.0))).andExpect(jsonPath("$[4].id", is(not(0))))
				.andExpect(jsonPath("$[4].id", is(notNullValue()))).andExpect(jsonPath("$[4].date", is("2024-05-11")))
				.andExpect(jsonPath("$[4].ticketNumber", is("00001-23226")))
				.andExpect(jsonPath("$[4].provider", is("Bengala"))).andExpect(jsonPath("$[4].inputNumber", is("212")))
				.andExpect(jsonPath("$[4].ticketTotal", is(2500.50))).andExpect(jsonPath("$[5].id", is(not(0))))
				.andExpect(jsonPath("$[5].id", is(notNullValue()))).andExpect(jsonPath("$[5].subtotal", is(6500.5)))
				.andExpect(jsonPath("$[5].subtotalTitle", is("SubTotal"))).andExpect(jsonPath("$[6].id", is(not(0))))
				.andExpect(jsonPath("$[6].id", is(notNullValue()))).andExpect(jsonPath("$[6].date", is("2024-05-10")))
				.andExpect(jsonPath("$[6].ticketNumber", is("00001-25223")))
				.andExpect(jsonPath("$[6].provider", is("La Comarca S.R.L")))
				.andExpect(jsonPath("$[6].inputNumber", is("219"))).andExpect(jsonPath("$[6].ticketTotal", is(3000.50)))
				.andExpect(jsonPath("$[7].id", is(not(0)))).andExpect(jsonPath("$[7].id", is(notNullValue())))
				.andExpect(jsonPath("$[7].subtotal", is(3000.5)))
				.andExpect(jsonPath("$[7].subtotalTitle", is("SubTotal"))).andReturn();
	}

	@Test
	@Order(22)
	void getSmallBoxTotalWithUserPedro() throws Exception {
		this.mockMvc
				.perform(get("http://localhost:8080/api/v1/smallbox/containers/{containerId}", container1Id)
						.header("Authorization", "Bearer " + userPedrojwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$.total", is(18501.0)));
	}

	@Test
	@Order(23)
	void updateOrganizationDirSitDeCalle() throws Exception {
		mvcResult = this.mockMvc
				.perform(put("http://localhost:8080/api/v1/smallbox/organization/update-organization").content(
						"{\"id\":1,\"organizationName\":\"Dir. de personas en situacion de calle\",\"organizationNumber\":5,\"responsibleId\":2,\"maxRotation\":9,\"maxAmount\":60000}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Dir. de personas en situacion de calle")))
				.andExpect(jsonPath("$.maxRotation", is(9))).andExpect(jsonPath("$.maxAmount", is(60000)))
				.andExpect(jsonPath("$.responsible", is("Carlos Heimerdinger"))).andReturn();
		assertThat(oldSitDeCalleOrg.getOrganizationName()).isNotEqualTo("Dir. de personas en situacion de calle");
		assertThat(oldSitDeCalleOrg.getResponsible().getId()).isNotEqualTo(hemerdingerId);
		assertThat(oldSitDeCalleOrg.getMaxAmount()).isLessThan(new BigDecimal(60000));
		assertThat(oldSitDeCalleOrg.getMaxRotation()).isGreaterThan(9);
	}

	@Test
	@Order(24)
	void updateUserPedro29Email() throws Exception {
		this.mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/registration/update").content(
				"{\"id\":2,\"name\":\"Pedro\",\"lastname\":\"Mozart\",\"username\":\"pedro29\",\"email\":\"car_moz@hotmail.com\",\"password\":\"Xta2929341\"}")
				.header("Authorization", "Bearer " + jwtToken).param("authority", "USER")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath(("$.name"), is("Pedro"))).andExpect(jsonPath("$.password", is(nullValue())));

	}

	@Test
	@Order(25)
	void checkUserPedro29EmailValue() throws Exception {
		AppUser userPedro29 = appUserRepository.findByUsername("pedro29")
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el usuario"));
		assertThat(userPedro29.getId()).isGreaterThan(0);
		assertThat(userPedro29.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("USER");
		assertThat(userPedro29.getUsername()).isEqualTo("pedro29");
		assertThat(userPedro29.getName()).isEqualTo("Pedro");
		assertThat(userPedro29.getLastname()).isEqualTo("Mozart");
		assertThat(userPedro29.getEmail()).isEqualTo("car_moz@hotmail.com");
	}

	@Test
	@Order(26)
	void createResponsiblePierpaoli() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-responsible")
						.content("{\"name\":\"Roxana\",\"lastname\":\"Pierpaoli\"}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is("Roxana")))
				.andExpect(jsonPath("$.lastname", is("Pierpaoli"))).andReturn();
	}

	@Test
	@Order(27)
	void addOrganizationSecDesSocial() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-organization").content(
						"{\"organizationName\":\"Secretaria de desarrollo social\",\"organizationNumber\":3,\"responsibleId\":4,\"maxRotation\":12,\"maxAmount\":100000}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Secretaria de desarrollo social")))
				.andExpect(jsonPath("$.maxRotation", is(12))).andExpect(jsonPath("$.maxAmount", is(100000)))
				.andExpect(jsonPath("$.responsible", is("Roxana Pierpaoli"))).andReturn();
	}

	@Test
	@Order(28)
	void assignParentOrganiationToDirSitDeCalle() throws Exception {
		this.mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/organization/set-parent-organizations")
						.content("{\"mainOrganizationId\":1,\"parentOrganizationIds\":[3]}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.mainOrganizationName", is("Dir. de personas en situacion de calle")))
				.andExpect(jsonPath("$.parentOrganizationNames", is(List.of("Secretaria de desarrollo social"))));
	}

	@Test
	@Order(29)
	void createSuperUserMiguel248() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/registration/register").content(
				"{\"name\":\"Miguel\",\"lastname\":\"Fernandez\",\"username\":\"miguel248\",\"email\":\"miguelCapo@gmail.com\",\"password\":\"Lsm4234ib\"}")
				.header("Authorization", "Bearer " + jwtToken).param("authority", "SUPERUSER")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath(("$.name"), is("Miguel"))).andExpect(jsonPath("$.password", is(nullValue())));
	}

	@Test
	@Order(30)
	void assignSecDesSocialToMiguel248() throws Exception {
		AppUser userMiguel248 = appUserRepository.findByUsername("miguel248").get();
		mvcResult = this.mockMvc
				.perform(put("http://localhost:8080/api/v1/smallbox/organization/add-organization")
						.param("userId", Long.toString(userMiguel248.getId())).param("organizationsId", "3")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
		String stringResult = mvcResult.getResponse().getContentAsString();
		boolean doesContain = stringResult
				.contains("El usuario: Miguel Fernandez Tiene asignada las siguientes dependencias:"
						+ " Secretaria de desarrollo social");
		assertTrue(doesContain);
	}

	@Test
	@Order(31)
	void loginWithSuperUserMiguel248() throws Exception {
		mvcResult = mockMvc
				.perform(post("http://localhost:8080/api/v1/smallbox/authorization/login")
						.content("{\"username\":\"miguel248\",\"password\":\"Lsm4234ib\"}")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.userId", is(notNullValue()))).andExpect(jsonPath("$.token", is(notNullValue())))
				.andReturn();

		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		for (String str : list) {
			if (str.length() > 200) {
				superUserMiguel248JwtToken = str;
			}
		}

	}

	@Test
	@Order(32)
	void selectSuperUserMiguel248MainOrganization() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/organization/set-user-organization")
				.param("organizationId", "3").param("userId", "3")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", is(3)));
	}

	@Test
	@Order(33)
	void whenTryToCreateResponsibleWithSuperUserMiguel248_Return403Forbidden() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-responsible")
				.content("{\"name\":\"Alfonso\",\"lastname\":\"Gomez\"}")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(403)).andReturn();
	}

	@Test
	@Order(34)
	void whenTryToCreateOrganizationWithSuperUserMiguel248_Return403Forbidden() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/organization/new-organization").content(
				"{\"organizationName\":\"Dir. de Presuspuesto\",\"organizationNumber\":14,\"responsibleId\":1,\"maxRotation\":12,\"maxAmount\":45000}")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(403));

	}

	@Test
	@Order(35)
	void whenTryToRegisterUserWithSuperUserMiguel248_Return403Forbidden() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/registration/register").content(
				"{\"name\":\"Mariano\",\"lastname\":\"Pergamino\",\"username\":\"marper\",\"email\":\"mar@gmail.com\",\"password\":\"123\"}")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken).param("authority", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(403));

	}

	@Test
	@Order(36)
	void uploadPurchaseOrder365WithSuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "oc-365.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\oc-365.pdf").getContentAsByteArray());
		mockMvc.perform(
				multipart("http://localhost:8080/api/v1/smallbox/purchase-order/collect-purchase-order-pdf").file(file)
						.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.orderNumber", is(365)))
				.andExpect(jsonPath("$.date").value("2024-02-19"))
				.andExpect(jsonPath("$.purchaseOrderTotal", is(295600.00)))
				.andExpect(jsonPath("$.items[0].code", is("2.1.1.00788.0013")))
				.andExpect(jsonPath("$.items[0].quantity", is(15)))
				.andExpect(jsonPath("$.items[0].measureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.items[0].itemDetail").value(containsString("GALLETITAS")))
				.andExpect(jsonPath("$.items[0].programaticCat", is("39.00.00")))
				.andExpect(jsonPath("$.items[0].unitCost", is(3100.00000)))
				.andExpect(jsonPath("$.items[0].totalEstimatedCost", is(46500.00)))
				.andExpect(jsonPath("$.items[1].code", is("2.1.1.00705.0035")))
				.andExpect(jsonPath("$.items[1].quantity", is(8)))
				.andExpect(jsonPath("$.items[1].measureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.items[1].itemDetail").value(containsString("LECHE")))
				.andExpect(jsonPath("$.items[1].programaticCat", is("39.00.00")))
				.andExpect(jsonPath("$.items[1].unitCost", is(5000.00000)))
				.andExpect(jsonPath("$.items[1].totalEstimatedCost", is(40000.00)));
	}

	@Test
	@Order(37)
	void uploadPurchaseOrder340WithSuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "oc-340.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\oc-340.pdf").getContentAsByteArray());
		mockMvc.perform(
				multipart("http://localhost:8080/api/v1/smallbox/purchase-order/collect-purchase-order-pdf").file(file)
						.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.orderNumber", is(340)))
				.andExpect(jsonPath("$.date").value("2024-02-16"))
				.andExpect(jsonPath("$.purchaseOrderTotal", is(905700.00)))
				.andExpect(jsonPath("$.items[0].code", is("2.1.1.00788.0091")))
				.andExpect(jsonPath("$.items[0].quantity", is(350)))
				.andExpect(jsonPath("$.items[0].measureUnit", is("UNIDAD")))
				.andExpect(jsonPath("$.items[0].itemDetail").value(containsString("GALLETITAS")))
				.andExpect(jsonPath("$.items[0].programaticCat", is("38.01.00")))
				.andExpect(jsonPath("$.items[0].unitCost", is(1580.00000)))
				.andExpect(jsonPath("$.items[0].totalEstimatedCost", is(553000.00)))
				.andExpect(jsonPath("$.items[1].code", is("2.1.1.00439.0001")))
				.andExpect(jsonPath("$.items[1].quantity", is(50)))
				.andExpect(jsonPath("$.items[1].measureUnit", is("UNIDAD")))
				.andExpect(jsonPath("$.items[1].itemDetail").value(containsString("ACEITE")))
				.andExpect(jsonPath("$.items[1].programaticCat", is("38.01.00")))
				.andExpect(jsonPath("$.items[1].unitCost", is(4500.00)))
				.andExpect(jsonPath("$.items[1].totalEstimatedCost", is(225000.00)));
	}

	@Test
	@Order(38)
	void uploadSupply551WithSuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "sum-551.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\sum-551.pdf").getContentAsByteArray());
		mockMvc.perform(multipart("http://localhost:8080/api/v1/smallbox/supply/collect-supply-pdf").file(file)
				.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.supplyNumber", is(551)))
				.andExpect(jsonPath("$.date").value("2024-02-06"))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.estimatedTotalCost", is(43697001.00)))
				.andExpect(jsonPath("$.supplyItems[0].code", is("5.1.4.03451.0001")))
				.andExpect(jsonPath("$.supplyItems[0].programaticCat", is("01.10.00")))
				.andExpect(jsonPath("$.supplyItems[0].quantity", is(5500)))
				.andExpect(jsonPath("$.supplyItems[0].measureUnit", is("KILOGRAMO")))
				.andExpect(jsonPath("$.supplyItems[0].itemDetail").value(containsString("AZUCAR")))
				.andExpect(jsonPath("$.supplyItems[0].unitCost", is(1280.01000)))
				.andExpect(jsonPath("$.supplyItems[0].totalEstimatedCost", is(7040055.00)))
				.andExpect(jsonPath("$.supplyItems[1].code", is("5.1.4.03503.0003")))
				.andExpect(jsonPath("$.supplyItems[1].programaticCat", is("01.10.00")))
				.andExpect(jsonPath("$.supplyItems[1].quantity", is(5000)))
				.andExpect(jsonPath("$.supplyItems[1].measureUnit", is("CADA-UNO")))
				.andExpect(jsonPath("$.supplyItems[1].itemDetail").value(containsString("FLAN")))
				.andExpect(jsonPath("$.supplyItems[1].unitCost", is(600.00)))
				.andExpect(jsonPath("$.supplyItems[1].totalEstimatedCost", is(3000000.00)));
	}
	@Test
	@Order(39)
	void setSupply551OrganizationApplicantWithSuperUserMiguel248()throws Exception{
		OrganizationDto organizationDto = new OrganizationDto();
		organizationDto.setId(1l);
		mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/supply/set-supply-organization-applicant")
				.content(gson.toJson(organizationDto)).param("supplyId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$",is( "Dir. de personas en situacion de calle")));
	}

	@Test
	@Order(40)
	void uploadSupply1043_WithSuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "sum-1043.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\sum-1043.pdf").getContentAsByteArray());
		mockMvc.perform(multipart("http://localhost:8080/api/v1/smallbox/supply/collect-supply-pdf").file(file)
				.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.supplyNumber", is(1043)))
				.andExpect(jsonPath("$.date").value("2023-03-08"))
				.andExpect(jsonPath("$.estimatedTotalCost", is(5500000.00)))
				.andExpect(jsonPath("$.supplyItems[0].code", is("5.1.4.07776.0001")))
				.andExpect(jsonPath("$.supplyItems[0].programaticCat", is("32.06.00")))
				.andExpect(jsonPath("$.supplyItems[0].quantity", is(5000)))
				.andExpect(jsonPath("$.supplyItems[0].measureUnit", is("UNIDAD")))
				.andExpect(jsonPath("$.supplyItems[0].itemDetail").value(containsString("BOLSON DE VEDURAS")))
				.andExpect(jsonPath("$.supplyItems[0].unitCost", is(1100.00)))
				.andExpect(jsonPath("$.supplyItems[0].totalEstimatedCost", is(5500000.00)));

	}
	@Test
	@Order(41)
	void setSupply1043ganizationApplicantWithSuperUserMiguel248()throws Exception{
		OrganizationDto organizationDto = new OrganizationDto();
		organizationDto.setId(1l);
		mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/supply/set-supply-organization-applicant")
				.content("{\"id\":1}").param("supplyId", "2")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$",is( "Dir. de personas en situacion de calle")));
	}

	@Test
	@Order(42)
	void createDepositAvellaneda_WithSuperUserMiguel248() throws Exception {
		mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-control/create-deposit").content(
				"{\"name\":\"AVELLANEDA\",\"streetName\":\"Avellaneda\",\"houseNumber\":\"5432\",\"organizationId\":3}")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$").value("AVELLANEDA"));
	}

	@Test
	@Order(43)
	void checkCreatedDepositAvellanedaValues() throws Exception {
		Deposit deposit = depositRepository.findById(1L)
				.orElseThrow(() -> new ItemNotFoundException("Deposit not found"));
		assertThat(deposit.getId()).isGreaterThan(0l);
		assertThat(deposit.getName()).isEqualTo("AVELLANEDA");
		assertThat(deposit.getStreetName()).isEqualTo("Avellaneda");
		assertThat(deposit.getHouseNumber()).isEqualTo("5432");
		assertThat(deposit.getOrganization().getId()).isEqualTo(3L);
	}

	@Test
	@Order(44)
	void setCurrentDepositToSuperUserMiguel248_OrganizationSecDesSocial() throws Exception {
		mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/deposit-control/set-current-deposit").content("1")
				.param("userId", "3").param("organizationId", "3")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200)).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("AVELLANEDA")));
	}

	@Test
	@Order(45)
	void getCurrentSuperUserMiguel248_Organization_DepositId() throws Exception {
		mockMvc.perform(
				get("http://localhost:8080/api/v1/smallbox/deposit-control/get-current-deposit").param("userId", "3")
						.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("AVELLANEDA")));
	}

	@Test
	@Order(46)
	void loadPurchaseOrder365ToDeposit_SuperUserMiguel248() throws Exception {
		mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/purchase-order/load-order-to-deposit").content("1")
				.param("depositId", "1").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].depositItemCode", is("2.1.1.00788.0013")))
				.andExpect(jsonPath("$.[0].depositItemDescription").value(containsString("GALLETITAS TIPO VAINILLA")))
				.andExpect(jsonPath("$.[0].depositItemQuantity", is(15)))
				.andExpect(jsonPath("$.[0].depositItemMeasureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.[0].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[1].depositItemCode", is("2.1.1.00705.0035")))
				.andExpect(
						jsonPath("$.[1].depositItemDescription").value(containsString("LECHE ESTADO EN POLVO ENTERA")))
				.andExpect(jsonPath("$.[1].depositItemQuantity", is(8)))
				.andExpect(jsonPath("$.[1].depositItemMeasureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.[1].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[2].depositItemCode", is("2.1.1.00592.0001")))
				.andExpect(jsonPath("$.[2].depositItemDescription").value(containsString("LENTEJAS ESTADO NATURA")))
				.andExpect(jsonPath("$.[2].depositItemQuantity", is(15)))
				.andExpect(jsonPath("$.[2].depositItemMeasureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.[2].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[3].depositItemCode", is("2.1.1.00591.0002")))
				.andExpect(jsonPath("$.[3].depositItemDescription").value(containsString("POROTOS TIPO ALUBIA ESTADO")))
				.andExpect(jsonPath("$.[3].depositItemQuantity", is(20)))
				.andExpect(jsonPath("$.[3].depositItemMeasureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.[3].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[4].depositItemCode", is("2.1.1.00705.0036")))
				.andExpect(jsonPath("$.[4].depositItemDescription").value(containsString("LECHE ESTADO POLVO TIPO")))
				.andExpect(jsonPath("$.[4].depositItemQuantity", is(10)))
				.andExpect(jsonPath("$.[4].depositItemMeasureUnit", is("CAJA")))
				.andExpect(jsonPath("$.[4].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[5].depositItemCode", is("2.1.1.00511.0008")))
				.andExpect(jsonPath("$.[5].depositItemDescription").value(containsString("TE PRESENTACION CAJA")))
				.andExpect(jsonPath("$.[5].depositItemQuantity", is(10)))
				.andExpect(jsonPath("$.[5].depositItemMeasureUnit", is("CAJA")))
				.andExpect(jsonPath("$.[5].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[6].depositItemCode", is("2.1.1.00705.0012")))
				.andExpect(
						jsonPath("$.[6].depositItemDescription").value(containsString("LECHE ESTADO EN POLVO ENTERA")))
				.andExpect(jsonPath("$.[6].depositItemQuantity", is(8)))
				.andExpect(jsonPath("$.[6].depositItemMeasureUnit", is("CADA-UNO")))
				.andExpect(jsonPath("$.[6].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[7].depositItemCode", is("2.1.1.02113.0002")))
				.andExpect(jsonPath("$.[7].depositItemDescription").value(containsString("DULCE TIPO DE MEMBRILLO")))
				.andExpect(jsonPath("$.[7].depositItemQuantity", is(1)))
				.andExpect(jsonPath("$.[7].depositItemMeasureUnit", is("CADA-UNO")))
				.andExpect(jsonPath("$.[7].depositItemStatus", is("NUEVO")));

	}

	@Test
	@Order(47)
	void loadPurchaseOrder454With_SuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "oc-454.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\oc-454.pdf").getContentAsByteArray());
		mockMvc.perform(
				multipart("http://localhost:8080/api/v1/smallbox/purchase-order/collect-purchase-order-pdf").file(file)
						.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.orderNumber", is(454)))
				.andExpect(jsonPath("$.date").value("2024-02-23"))
				.andExpect(jsonPath("$.items[0].totalEstimatedCost", is(414558.60)))
				.andExpect(jsonPath("$.items[1].totalEstimatedCost", is(473932.80)));
	}

	@Test
	@Order(48)
	void loadPurchaseOrder454ToDepositControl_SuperUserMiguel248() throws Exception {
		mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/purchase-order/load-order-to-deposit").content("3")
				.param("depositId", "1").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].depositItemCode", is("2.1.1.03311.0003")))
				.andExpect(jsonPath("$.[0].depositItemDescription").value(containsString("JUGO TIPO JUGO DE FRUTA")))
				.andExpect(jsonPath("$.[0].depositItemQuantity", is(60)))
				.andExpect(jsonPath("$.[0].depositItemMeasureUnit", is("CAJA")))
				.andExpect(jsonPath("$.[0].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[1].depositItemCode", is("2.1.1.00704.0008")))
				.andExpect(jsonPath("$.[1].depositItemDescription")
						.value(containsString("ALFAJOR RELLENO DULCE DE LECHE")))
				.andExpect(jsonPath("$.[1].depositItemQuantity", is(60)))
				.andExpect(jsonPath("$.[1].depositItemMeasureUnit", is("CAJA")))
				.andExpect(jsonPath("$.[1].depositItemStatus", is("NUEVO")))
				.andExpect(jsonPath("$.[8].depositItemCode", is("2.1.1.02113.0002")))
				.andExpect(jsonPath("$.[8].depositItemDescription").value(containsString("DULCE TIPO DE MEMBRILLO")))
				.andExpect(jsonPath("$.[8].depositItemQuantity", is(4)))
				.andExpect(jsonPath("$.[8].depositItemMeasureUnit", is("CADA-UNO")))
				.andExpect(jsonPath("$.[8].depositItemStatus", is("ACTUALIZADO")));
	}

	@Test
	@Order(49)
	void FindSecDesSocialPurchaseOrderList_SuperUserMiguel248() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/purchase-order/find-all-orders-by-org")
				.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].id", is(notNullValue()))).andExpect(jsonPath("$.[0].orderNumber", is(454)))
				.andExpect(jsonPath("$.[0].date", is("2024-02-23")))
				.andExpect(jsonPath("$.[0].dependency", is("Coordinación de Protección de Niñez")))
				.andExpect(jsonPath("$.[0].purchaseOrderTotal", is(1351918.75)))
				.andExpect(jsonPath("$.[0].loadedToDeposit", is(true)))
				.andExpect(jsonPath("$.[1].id", is(notNullValue()))).andExpect(jsonPath("$.[1].orderNumber", is(365)))
				.andExpect(jsonPath("$.[1].date", is("2024-02-19")))
				.andExpect(jsonPath("$.[1].dependency", is("Dirección de Reinserción Social")))
				.andExpect(jsonPath("$.[1].purchaseOrderTotal", is(295600.00)))
				.andExpect(jsonPath("$.[1].loadedToDeposit", is(true)))
				.andExpect(jsonPath("$.[2].id", is(notNullValue()))).andExpect(jsonPath("$.[2].orderNumber", is(340)))
				.andExpect(jsonPath("$.[2].date", is("2024-02-16")))
				.andExpect(jsonPath("$.[2].dependency", is("Coordinación de Promoción de Niñez")))
				.andExpect(jsonPath("$.[2].purchaseOrderTotal", is(905700.00)))
				.andExpect(jsonPath("$.[2].loadedToDeposit", is(false)));
	}

	@Test
	@Order(50)
	void findPurchaseOrder340Items_SuperUserMiguel248() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/purchase-order/find-order-items")
				.param("purchaseOrderId", "2").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].id", is(notNullValue())))
				.andExpect(jsonPath("$.[0].code", is("2.1.1.00788.0091")))
				.andExpect(jsonPath("$.[0].quantity", is(350))).andExpect(jsonPath("$.[1].id", is(notNullValue())))
				.andExpect(jsonPath("$.[1].code", is("2.1.1.00439.0001")))
				.andExpect(jsonPath("$.[1].quantity", is(50)));
	}

	@Test
	@Order(51)
	void findSupply551Items_SuperUserMiguel248() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/supply//find-supply-items").param("supplyId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].id", is(notNullValue())))
				.andExpect(jsonPath("$.[0].code", is("5.1.4.03451.0001")))
				.andExpect(jsonPath("$.[0].quantity", is(5500))).andExpect(jsonPath("$.[1].id", is(notNullValue())))
				.andExpect(jsonPath("$.[1].code", is("5.1.4.03503.0003")))
				.andExpect(jsonPath("$.[1].quantity", is(5000)));
	}

	@Test
	@Order(52)
	void deletePurchaseOrder340_SuperUserMiguel248() throws Exception {
		mockMvc.perform(
				delete("http://localhost:8080/api/v1/smallbox/purchase-order/delete-purchase-order/{orderId}", 2)
						.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$", is(340)));
	}

	@Test
	@Order(53)
	void whenTryToFindPurchaseOrder340Items_mustReturn417ExpectationFailed_SuperUserMiguel248() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/purchase-order/find-order-items")
				.param("purchaseOrderId", "2").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(417));

	}

	@Test
	@Order(54)
	void deleteSupply551_SuperUserMiguel248() throws Exception {
		mockMvc.perform(delete("http://localhost:8080/api/v1/smallbox/supply/delete-supply/{supplyId}", 1)
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200)).andExpect(jsonPath("$", is(551)));
	}

	@Test
	@Order(55)
	void whenTryToFindSupply551Items_mustReturn417ExpectationFailed_Miguel248() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/supply/find-supply-items").param("supplyId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(417));

	}

	@Test
	@Order(56)
	void loadPurchaseOrder534_SuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "oc-534.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\oc-534.pdf").getContentAsByteArray());
		mockMvc.perform(
				multipart("http://localhost:8080/api/v1/smallbox/purchase-order/collect-purchase-order-pdf").file(file)
						.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.orderNumber", is(534)))
				.andExpect(jsonPath("$.date").value("2024-02-29"))
				.andExpect(jsonPath("$.items[0].totalEstimatedCost", is(427500.00)))
				.andExpect(jsonPath("$.items[1].totalEstimatedCost", is(511800.00)))
				.andExpect(jsonPath("$.purchaseOrderTotal", is(939300.00)));
	}

	private long oldDepositCount;
	private DepositControl oldDepositItemSelected;

	@Test
	@Order(57)
	void ImportExcelItemsToDeposit_SuperUserMiguel248() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "control_excel3-v3-test.xls", "application/xls",
				new ClassPathResource("\\pdf-test\\control_excel3-v3-test.xls").getContentAsByteArray());
		mockMvc.perform(
				multipart("http://localhost:8080/api/v1/smallbox/deposit-control/excel-order-comparator").file(file)
						.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.[0].excelItemDto.itemDescription")
						.value(containsString("PAÑALES BEBE TALLE XG POR 44 UNI")))
				.andExpect(jsonPath("$.[0].excelItemDto.itemMeasureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.[0].excelItemDto.itemQuantity", is(9285)))
				.andExpect(jsonPath("$.[0].purchaseOrderItemCandidateDtos[0].itemDetail", is("No encontrado")))
				.andExpect(jsonPath("$.[1].excelItemDto.itemDescription")
						.value(containsString("PAÑALES BEBE TALLE XG POR 28 UNI")))
				.andExpect(jsonPath("$.[1].excelItemDto.itemMeasureUnit", is("PAQUETE")))
				.andExpect(jsonPath("$.[1].excelItemDto.itemQuantity", is(16)))
				.andExpect(jsonPath("$.[70].excelItemDto.itemDescription").value(containsString("ZAPATILLA TALLE 45")))
				.andExpect(jsonPath("$.[70].excelItemDto.itemMeasureUnit", is("UNIDAD")))
				.andExpect(jsonPath("$.[70].excelItemDto.itemQuantity", is(30)))
				.andExpect(jsonPath("$.[70].purchaseOrderItemCandidateDtos[0].code", is("2.2.2.00828.0102")))
				.andExpect(jsonPath("$.[70].purchaseOrderItemCandidateDtos[0].itemDetail")
						.value(containsString("ZAPATILLAS USO UNISEX")))
				.andExpect(jsonPath("$.[70].purchaseOrderItemCandidateDtos[1].code", is("2.2.2.00828.0101")))
				.andExpect(jsonPath("$.[70].purchaseOrderItemCandidateDtos[1].itemDetail")
						.value(containsString("ZAPATILLAS USO UNISEX")));

		Deposit avellanedaDepo = depositRepository.findById(1l).get();
		oldDepositItemSelected = depositControlRepository.findByItemCodeAndDeposit("2.1.1.00482.0014", avellanedaDepo)
				.get();

	}

	private List<ExcelItemDto> excelSelectedItems;

	@Test
	@Order(58)
	void createExcelItemsSelectedList() {
		excelSelectedItems = new ArrayList<ExcelItemDto>();
		ExcelItemDto item1 = new ExcelItemDto();
		item1.setExcelItemId(71);
		item1.setItemQuantity(30);
		item1.setPurchaseOrderItemId(30);
		ExcelItemDto item2 = new ExcelItemDto();
		item2.setExcelItemId(92);
		item2.setItemQuantity(50);
		item2.setPurchaseOrderItemId(21);
		excelSelectedItems.add(item1);
		excelSelectedItems.add(item2);
	}

	private Gson gson = new Gson();

	@Test
	@Order(59)
	void createOrUpdateExcelItemsToDepositControl_SuperUserMiguel248() throws Exception {
		mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-control/save-excel-items-to-deposit")
				.content(gson.toJson(excelSelectedItems)).param("organizationId", "3").param("depositId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.[0].itemCode", is("2.1.1.00482.0014")))
				.andExpect(jsonPath("$.[0].quantity", is(110)))
				.andExpect(jsonPath("$.[0].id").value(oldDepositItemSelected.getId()))
				.andExpect(jsonPath("$.[1].itemCode", is("2.2.2.00828.0102")))
				.andExpect(jsonPath("$.[1].quantity", is(30)));

	}

	private List<BigBagItemDto> bigBagNavidadItemDtos;

	@Test
	@Order(60)
	void addBigBagList() throws Exception {
		Deposit deposit = depositRepository.findById(1l)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el depo."));
		bigBagNavidadItemDtos = depositControlRepository
				.findAllByItemCodeInAndDeposit(List.of("2.1.1.03311.0003", "2.1.1.00704.0008", "2.1.1.02113.0002"),
						deposit)
				.stream().map(depoItem -> {
					BigBagItemDto itemDto = new BigBagItemDto();
					itemDto.setCode(depoItem.getItemCode());
					itemDto.setMeasureUnit(depoItem.getMeasureUnit());
					itemDto.setDescription(depoItem.getItemDescription());
					itemDto.setDepositControlId(depoItem.getId());
					return itemDto;
				}).toList();

	}

	@Test
	@Order(61)
	void createBigBagNavidad_SuperUserMiguel248() throws Exception {
		BigBagDto bigBagDto = new BigBagDto();
		bigBagDto.setName("Navidad");
		bigBagDto.setItems(bigBagNavidadItemDtos);

		mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-control/create-big-bag")
				.content(gson.toJson(bigBagDto)).param("organizationId", "3")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is("Navidad")))
				.andExpect(jsonPath("$.creationDate").isNotEmpty())
				.andExpect(jsonPath("$.items[0].id", is(notNullValue())))
				.andExpect(jsonPath("$.items[1].id", is(notNullValue())))
				.andExpect(jsonPath("$.items[2].id", is(notNullValue())));

	}

	private int minorQuantityDepositItemNavidad;

	@Test
	@Order(62)
	void calculateTotalBigBagNavidadQuantityAvailable_SuperUserMiguel248() throws Exception {
		Deposit deposit = depositRepository.findById(1l)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el depo."));
		minorQuantityDepositItemNavidad = depositControlRepository
				.findAllByItemCodeInAndDeposit(List.of("2.1.1.03311.0003", "2.1.1.00704.0008", "2.1.1.02113.0002"),
						deposit)
				.stream().map(item -> item.getQuantity()).min(Integer::compareTo).get();
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-control/calculate-big-bag-total-quantity")
				.param("bigBagId", "1").param("depositId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$", is(minorQuantityDepositItemNavidad)));
	}

	private List<BigBagItemDto> bigBagAñoNuevoItemDtos;

	@Test
	@Order(63)
	void addBigBagAñoNuevo_SuperUserMiguel248() throws Exception {
		Deposit deposit = depositRepository.findById(1l)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el depo."));
		bigBagAñoNuevoItemDtos = depositControlRepository
				.findAllByItemCodeInAndDeposit(List.of("2.1.1.00705.0035", "2.1.1.00592.0001", "2.1.1.00591.0002"),
						deposit)
				.stream().map(depoItem -> {
					BigBagItemDto itemDto = new BigBagItemDto();
					itemDto.setCode(depoItem.getItemCode());
					itemDto.setMeasureUnit(depoItem.getMeasureUnit());
					itemDto.setDescription(depoItem.getItemDescription());
					itemDto.setDepositControlId(depoItem.getId());
					return itemDto;
				}).toList();
	}

	@Test
	@Order(64)
	void createBigBagAñoNuevo_SuperUserMiguel248() throws Exception {
		BigBagDto bigBagDto = new BigBagDto();
		bigBagDto.setName("Año nuevo");
		bigBagDto.setItems(bigBagAñoNuevoItemDtos);

		mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-control/create-big-bag")
				.content(gson.toJson(bigBagDto)).param("organizationId", "3")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is("Año nuevo")))
				.andExpect(jsonPath("$.creationDate").isNotEmpty())
				.andExpect(jsonPath("$.items[0].id", is(notNullValue())))
				.andExpect(jsonPath("$.items[1].id", is(notNullValue())))
				.andExpect(jsonPath("$.items[2].id", is(notNullValue())));

	}

	private int minorQuantityDepositItemAñoNuevo;

	@Test
	@Order(65)
	void calculateTotalBigBagAñoNuevoQuantityAvailable_SuperUserMiguel248() throws Exception {
		Deposit deposit = depositRepository.findById(1l)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el depo."));
		minorQuantityDepositItemAñoNuevo = depositControlRepository
				.findAllByItemCodeInAndDeposit(List.of("2.1.1.00705.0035", "2.1.1.00592.0001", "2.1.1.00591.0002"),
						deposit)
				.stream().map(item -> item.getQuantity()).min(Integer::compareTo).get();
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-control/calculate-big-bag-total-quantity")
				.param("bigBagId", "2").param("depositId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$", is(minorQuantityDepositItemAñoNuevo)));
	}

	@Test
	@Order(66)
	void updateBigBagItemQuantity_SuperUserMiguel248() throws Exception {
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-control/update-big-bag-item-quantity")
				.param("bigBagItemId", "5").param("quantity", "3")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200)).andExpect(jsonPath("$.id", is(5)))
				.andExpect(jsonPath("$.quantity", is(3)));
	}

	@Test
	@Order(67)
	void calculateTotalBigBagAñoNuevoQuantityAvailableWithModifiedItemQuantity_SuperUserMiguel248() throws Exception {
		Deposit deposit = depositRepository.findById(1l)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el depo."));
		minorQuantityDepositItemAñoNuevo = depositControlRepository
				.findAllByItemCodeInAndDeposit(List.of("2.1.1.00705.0035", "2.1.1.00592.0001", "2.1.1.00591.0002"),
						deposit)
				.stream().map(item -> item.getQuantity()).min(Integer::compareTo).get();
		mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-control/calculate-big-bag-total-quantity")
				.param("bigBagId", "2").param("depositId", "1")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$", is(minorQuantityDepositItemAñoNuevo / 3)));

	}
	@Test
	@Order(68)
	void uploadSupply551WithSuperUserMiguel248Again() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "sum-551.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\sum-551.pdf").getContentAsByteArray());
		mockMvc.perform(multipart("http://localhost:8080/api/v1/smallbox/supply/collect-supply-pdf").file(file)
				.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.supplyNumber", is(551)))
				.andExpect(jsonPath("$.date").value("2024-02-06"))
				.andExpect(jsonPath("$.id", is(3)))
				.andExpect(jsonPath("$.estimatedTotalCost", is(43697001.00)))
				.andExpect(jsonPath("$.supplyItems[0].code", is("5.1.4.03451.0001")))
				.andExpect(jsonPath("$.supplyItems[0].programaticCat", is("01.10.00")))
				.andExpect(jsonPath("$.supplyItems[0].quantity", is(5500)))
				.andExpect(jsonPath("$.supplyItems[0].measureUnit", is("KILOGRAMO")))
				.andExpect(jsonPath("$.supplyItems[0].itemDetail").value(containsString("AZUCAR")))
				.andExpect(jsonPath("$.supplyItems[0].unitCost", is(1280.01000)))
				.andExpect(jsonPath("$.supplyItems[0].totalEstimatedCost", is(7040055.00)))
				.andExpect(jsonPath("$.supplyItems[1].code", is("5.1.4.03503.0003")))
				.andExpect(jsonPath("$.supplyItems[1].programaticCat", is("01.10.00")))
				.andExpect(jsonPath("$.supplyItems[1].quantity", is(5000)))
				.andExpect(jsonPath("$.supplyItems[1].measureUnit", is("CADA-UNO")))
				.andExpect(jsonPath("$.supplyItems[1].itemDetail").value(containsString("FLAN")))
				.andExpect(jsonPath("$.supplyItems[1].unitCost", is(600.00)))
				.andExpect(jsonPath("$.supplyItems[1].totalEstimatedCost", is(3000000.00)));
	}
	@Test
	@Order(69)
	void setSupply551OrganizationApplicantWithSuperUserMiguel248Again()throws Exception{
		OrganizationDto organizationDto = new OrganizationDto();
		organizationDto.setId(1l);
		mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/supply/set-supply-organization-applicant")
				.content(gson.toJson(organizationDto)).param("supplyId", "3")
				.header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$",is( "Dir. de personas en situacion de calle")));
	}
	
	
	@Test
	@Order(70)
	void createDepositRequestWithDirPersSitDeCalle_UserPedro()throws Exception{
		DepositRequestDto depositRequestDto = new DepositRequestDto();
		depositRequestDto.setId(1l);
		depositRequestDto.setMainOrganizationId(1);
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-request/create-request").content(
				gson.toJson(depositRequestDto))
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.requestDate", is(notNullValue())))
				.andExpect(jsonPath("$.mainOrganizationId", is(1)));
				
	}
	@Test
	@Order(71)
	void setDepositRequestDestinationOrganization_UserPedro()throws Exception{
		DepositRequestDto depositRequestDto = new DepositRequestDto();
		depositRequestDto.setId(1l);
		depositRequestDto.setDestinationOrganizationId(3l);
		this.mockMvc.perform(put("http://localhost:8080/api/v1/smallbox/deposit-request/set-destination-organization")
				.content(gson.toJson(depositRequestDto))
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.requestDate", is(notNullValue())))
				.andExpect(jsonPath("$.destinationOrganizationName", is("Secretaria de desarrollo social")))
				.andExpect(jsonPath("$.destinationOrganizationId", is(3)))
				.andExpect(jsonPath("$.mainOrganizationId", is(1)));
	}
	@Test
	@Order(72)
	void checkSupplyOrganizationApplicant()throws Exception{
		Organization organization = organizationRepository.findById(3l).get();
		assertThat(organization.getId()).isEqualTo(3l);
		
		Supply supply551 = supplyRepository.findBySupplyNumberAndMainOrganization(551,organization).get();
		Supply supply1043 = supplyRepository.findBySupplyNumberAndMainOrganization(1043,organization).get();
		assertThat(supply551.getSupplyNumber()).isEqualTo(551);
		assertThat(supply551.getApplicantOrganization().getId()).isEqualTo(1l);
		assertThat(supply1043.getApplicantOrganization().getId()).isEqualTo(1l);
	}
	
	@Test
	@Order(73)
	void saveItemsToRequest_UserPedro()throws Exception{
		DepositRequestDto depositRequestDto = new DepositRequestDto();
		depositRequestDto.setId(1l);
		Map<String, Integer> itemSelectedMap = new HashMap<>();
		itemSelectedMap.put("5.1.4.03503.0003", 10);
		itemSelectedMap.put("5.1.4.03501.0001", 18);
		itemSelectedMap.put("5.1.4.07776.0001", 14);
		Organization destinationOrganization = organizationRepository.findById(3l).get();
		Organization mainOrganization = organizationRepository.findById(1l).get();
		List<Supply> supplies =  supplyRepository
				.findAllByMainOrganizationAndApplicantOrganization(destinationOrganization, mainOrganization);
		List<DepositControlRequestDto> requestItemDtos= supplyItemRepository.findAllBySupplyIn(supplies).stream()
				.map(supplyItem -> {
					if(itemSelectedMap.containsKey(supplyItem.getCode())) {
						DepositControlRequestDto depositControlRequestDto = new DepositControlRequestDto();
						depositControlRequestDto.setItemCode(supplyItem.getCode());
						depositControlRequestDto.setItemMeasureUnit(supplyItem.getMeasureUnit());
						depositControlRequestDto.setItemQuantity(itemSelectedMap.get(supplyItem.getCode()));
						depositControlRequestDto.setItemDescription(supplyItem.getItemDetail());
						return depositControlRequestDto;
					}
					DepositControlRequestDto depositControlRequestDto = new DepositControlRequestDto();
					depositControlRequestDto.setItemCode("B");
					return depositControlRequestDto;
				}).filter(f -> f.getItemCode()!="B").toList();
		depositRequestDto.setDepositControlRequestDtos(requestItemDtos);
		this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-request/save-items-to-request")
				.content(gson.toJson(depositRequestDto))
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.requestDate", is(notNullValue())))
				.andExpect(jsonPath("$.destinationOrganizationName", is("Secretaria de desarrollo social")))
				.andExpect(jsonPath("$.destinationOrganizationId", is(3)))
				.andExpect(jsonPath("$.mainOrganizationId", is(1)))
				.andExpect(jsonPath("$.depositControlRequestDtos[0].itemCode", is("5.1.4.07776.0001")))
				.andExpect(jsonPath("$.depositControlRequestDtos[0].itemQuantity", is(14)));
		
	}
	
	private String dirSitDeCalleRequestCode;
	@Test
	@Order(74)
	void sendRequest_UserPedro()throws Exception{
	  mvcResult = 	this.mockMvc.perform(post("http://localhost:8080/api/v1/smallbox/deposit-request/send-request")
				.param("depositRequestId", "1").header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(201))
			  .andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath("$").isString()).andReturn();
	  dirSitDeCalleRequestCode = mvcResult.getResponse().getContentAsString().replace("\"","");
	 
	}
	
	@Test
	@Order(75)
	void checkRequestReception_SuperUserMiguel248()throws Exception{
		this.mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-receiver/find-receivers-by-organization")
				.param("organizationId", "3").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].id", is(notNullValue())))
				.andExpect(jsonPath("$.[0].fromOrganizationName",is("Dir. de personas en situacion de calle")))
				.andExpect(jsonPath("$.[0].depositRequestCode", is(dirSitDeCalleRequestCode)));
	}
	@Test
	@Order(76)
	void checkRequestReceptionItems_SuperUserMiguel248()throws Exception{
		this.mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-receiver/find-all-control-receivers-by-receiver")
				.param("depositReceiverId", "1").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.[0].id", is(notNullValue())))
				.andExpect(jsonPath("$.[0].itemCode",is("5.1.4.07776.0001")))
				.andExpect(jsonPath("$.[0].itemQuantity",is(14)));
		DepositReceiver depositReceiver = depositReceiverRepository.findById(1l).get();
			List<DepositControlReceiver> receivers = depositControlReceiverRepository.findAllByDepositReceiver(depositReceiver);
			assertThat(receivers.stream().filter(item -> item.getItemCode().equals("5.1.4.03503.0003"))
					.findFirst().get().getItemQuantity()).isEqualTo(10);
			assertThat(receivers.stream().filter(item -> item.getItemCode().equals("5.1.4.03501.0001"))
					.findFirst().get().getItemQuantity()).isEqualTo(18);
			assertThat(receivers.stream().filter(item -> item.getItemCode().equals("5.1.4.07776.0001"))
					.findFirst().get().getItemQuantity()).isEqualTo(14);
	}
	@Test
	@Order(77)
	void getRequestComparationNote_SuperUserMiguel248()throws Exception{
		
		this.mockMvc.perform(get("http://localhost:8080/api/v1/smallbox/deposit-receiver/get-comparator-note")
				.param("depositReceiverId", "1").param("depositId", "1").header("Authorization", "Bearer " + superUserMiguel248JwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.items[0].requestItemCode",is("5.1.4.07776.0001")))
				.andExpect(jsonPath("$.items[0].requestItemQuantity",is(14)))
				.andExpect(jsonPath("$.items[0].controlItemCode",is("No encontrado")))
				.andExpect(jsonPath("$.items[1].requestItemCode",is("5.1.4.03503.0003")))
				.andExpect(jsonPath("$.items[1].requestItemQuantity",is(10)))
				.andExpect(jsonPath("$.items[1].controlItemCode",is("No encontrado")))
				.andExpect(jsonPath("$.items[2].requestItemCode",is("5.1.4.03501.0001")))
				.andExpect(jsonPath("$.items[2].requestItemQuantity",is(18)))
				.andExpect(jsonPath("$.items[2].controlItemCode",is("No encontrado")));
	}
}
