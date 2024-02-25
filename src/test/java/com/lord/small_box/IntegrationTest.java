package com.lord.small_box;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;
import com.lord.small_box.models.Input;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.repositories.AuthorityRepository;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
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
	private SmallBoxTypeRepository smallBoxTypeRepository;
	
	@Autowired
	private InputRepository inputRepository;

	@Autowired
	private MockMvc mockMvc;

	private MvcResult mvcResult;

	private String jwtToken;
	
	private String userPedrojwtToken;
	
	private Long lagunasId;

	private Long yanezId;

	private Long sitCalleId;
	
	private String container1Id;
	
	private String smallBoxRow3Id;

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
				.perform(post("http://localhost:8080/api/v1/small-box/authorization/login")
						.content("{\"username\":\"car\",\"password\":\"Dvf8650123\"}").contentType(MediaType.APPLICATION_JSON))
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
	void registerUser() throws Exception {
		this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/registration/register").content(
				"{\"name\":\"Pedro\",\"lastname\":\"Mozart\",\"username\":\"pedro29\",\"email\":\"car_moz@gmail.com\",\"password\":\"Xta2929341\"}")
				.header("Authorization", "Bearer " + jwtToken).param("authority", "USER")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath(("$.name"), is("Pedro"))).andExpect(jsonPath("$.username", is("pedro29")))
				.andExpect(jsonPath("$.password", is(nullValue())));

	}

	@Test
	@Order(3)
	void createResponsible() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/organization/new-responsible")
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
	void createOrganization() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/organization/new-organization").content(
						"{\"organizationName\":\"Dir  de personas en situacion de calle\",\"organizationNumber\":5,\"responsibleId\":1,\"maxRotation\":12,\"maxAmount\":45000}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Dir  de personas en situacion de calle")))
				.andExpect(jsonPath("$.maxRotation", is(12))).andExpect(jsonPath("$.maxAmount", is(45000)))
				.andExpect(jsonPath("$.responsible", is("Analia Lagunas"))).andReturn();
	}

	@Test
	@Order(5)
	void createNewResponsible() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/organization/new-responsible")
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
	@Order(6)
	void createNewOrganization() throws Exception {
		mvcResult = this.mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/organization/new-organization").content(
						"{\"organizationName\":\"Dir  de Logistica\",\"organizationNumber\":5,\"responsibleId\":2,\"maxRotation\":12,\"maxAmount\":45000}")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Dir  de Logistica")))
				.andExpect(jsonPath("$.maxRotation", is(12))).andExpect(jsonPath("$.maxAmount", is(45000)))
				.andExpect(jsonPath("$.responsible", is("Fabian Yanez"))).andReturn();
	}

	@Test
	@Order(7)
	void addOrganizationToUserPedro() throws Exception {

		mvcResult = this.mockMvc
				.perform(put("http://localhost:8080/api/v1/small-box/organization/add-organization")
						.param("userId", "2").param("organizationsId", "1,2")
						.header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
		String stringResult = mvcResult.getResponse().getContentAsString();
		boolean doesContain = stringResult
				.contains("El usuario: Pedro Mozart Tiene asignada las siguientes dependencias:"
						+ " Dir  de personas en situacion de calle, Dir  de Logistica");
		assertTrue(doesContain);

	}
	@Test
	@Order(8)
	void findAllOrganizationsByUser()throws Exception{
		
		mvcResult = this.mockMvc.perform(get("http://localhost:8080/api/v1/small-box/organization/all-orgs-by-user")
				.param("userId", "2")
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$[0].id", is(notNullValue())))
				.andExpect(jsonPath("$[0].id", is(not(0))))
				.andExpect(jsonPath("$[0].organizationName",is("Dir  de personas en situacion de calle")))
				.andExpect(jsonPath("$[0].responsible", is("Analia Lagunas")))
				.andExpect(jsonPath("$[0].responsibleId", is(notNullValue())))
				.andExpect(jsonPath("$[0].responsibleId", is(not(0))))
				.andExpect(jsonPath("$[0].currentRotation", is(0)))
				.andExpect(jsonPath("$[0].maxRotation", is(12)))
				.andExpect(jsonPath("$[0].maxAmount", is(45000.0)))
				.andExpect(jsonPath("$[1].id", is(notNullValue())))
				.andExpect(jsonPath("$[1].id", is(not(0))))
				.andExpect(jsonPath("$[1].organizationName",is("Dir  de Logistica")))
				.andExpect(jsonPath("$[1].responsible", is("Fabian Yanez")))
				.andExpect(jsonPath("$[1].responsibleId",is(notNullValue())))
				.andExpect(jsonPath("$[1].responsibleId", is(not(0))))
				.andExpect(jsonPath("$[1].currentRotation", is(0)))
				.andExpect(jsonPath("$[1].maxRotation", is(12)))
				.andExpect(jsonPath("$[1].maxAmount", is(45000.0)))
				.andReturn();
		
		
	}
	
	@Test
	@Order(9)
	void loginAsUserPedro()throws Exception{
		mvcResult = mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/authorization/login")
						.content("{\"username\":\"pedro29\",\"password\":\"Xta2929341\"}").contentType(MediaType.APPLICATION_JSON))
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
	@Order(10)
	void whenTryToCreateResponsibleWithUserPedroMustReturn403Forbidden()throws Exception{
		 this.mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/organization/new-responsible")
						.content("{\"name\":\"Alfonso\",\"lastname\":\"Gomez\"}")
						.header("Authorization", "Bearer " + userPedrojwtToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(403))
				.andReturn();
			
		
	}
	@Test
	@Order(11)
	void whenTryToCreateOrganizationWithUserPedroMustReturn403Forbidden()throws Exception{
		 this.mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/organization/new-organization").content(
						"{\"organizationName\":\"Dir. de Presuspuesto\",\"organizationNumber\":14,\"responsibleId\":1,\"maxRotation\":12,\"maxAmount\":45000}")
						.header("Authorization", "Bearer " + userPedrojwtToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(403));
				
			}
	
	@Test
	@Order(12)
	void whenTryToRegisterUserWithUserPedroMustReturn403Forbidden()throws Exception{
		this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/registration/register").content(
				"{\"name\":\"Mariano\",\"lastname\":\"Pergamino\",\"username\":\"marper\",\"email\":\"mar@gmail.com\",\"password\":\"123\"}")
				.header("Authorization", "Bearer " + userPedrojwtToken).param("authority", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(403));
			
	}
	
	@Test
	@Order(13)
	void createContainerWithUserPedro()throws Exception{
		Calendar now = Calendar.getInstance();
		mvcResult =  this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/containers/")
			 .content("{\"smallBoxType\":\"CHICA\",\"organizationId\":2}")
			 .header("Authorization", "Bearer " + userPedrojwtToken)
			 .contentType(MediaType.APPLICATION_JSON))
			 .andExpect(status().is(201)).andDo(MockMvcResultHandlers.print())
			 .andExpect(jsonPath("$.id", is(notNullValue())))
			 .andExpect(jsonPath("$.id", is(not(0))))
			 .andExpect(jsonPath("$.smallBoxType", is("CHICA")))
			 .andExpect(jsonPath("$.organization", is("Dir  de Logistica")))
			 .andExpect(jsonPath("$.responsible", is("Fabian Yanez")))
			 .andReturn();
		
		
		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		container1Id = Character.toString(list[2].charAt(1));
		
		
	}
	
	@Test
	@Order(14)
	void createSmallBoxRow1WithUserPedro()throws Exception{
		this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/smallboxes/new")
				.content("{\"date\":\"2023-02-10\",\"ticketNumber\":\"0001-2423\",\"provider\":\"Disalar\",\"inputId\":2,\"ticketTotal\":4000}")
				.param("containerId", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(201))
			.andExpect(jsonPath("$.id", is(notNullValue())))	
			.andExpect(jsonPath("$.id", is(not(0))))
			.andExpect(jsonPath("$.date", is("2023-02-10")))
			.andExpect(jsonPath("$.ticketNumber", is("0001-2423")))
			.andExpect(jsonPath("$.provider", is("Disalar")))
			.andExpect(jsonPath("$.description", is("Alimento para animales")))
			.andExpect(jsonPath("$.ticketTotal", is(4000)))
			.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));
		
	}
	@Test
	@Order(15)
	void createSmallBoxRow2WithUserPedro()throws Exception{
		this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/smallboxes/new")
				.content("{\"date\":\"2023-04-15\",\"ticketNumber\":\"0002-2223\",\"provider\":\"La Roma\",\"inputId\":1,\"ticketTotal\":3000}")
				.param("containerId", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(201))
			.andExpect(jsonPath("$.id", is(notNullValue())))	
			.andExpect(jsonPath("$.id", is(not(0))))
			.andExpect(jsonPath("$.date", is("2023-04-15")))
			.andExpect(jsonPath("$.ticketNumber", is("0002-2223")))
			.andExpect(jsonPath("$.provider", is("La Roma")))
			.andExpect(jsonPath("$.description", is("Alimento para personas")))
			.andExpect(jsonPath("$.ticketTotal", is(3000)))
			.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));
		
	}
	
	@Test
	@Order(16)
	void createSmallBoxRow3WithUserPedro()throws Exception{
		mvcResult =  this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/smallboxes/new")
				.content("{\"date\":\"2024-05-11\",\"ticketNumber\":\"00001-23223\",\"provider\":\"Bengala\",\"inputId\":2,\"ticketTotal\":2500.50}")
				.param("containerId", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(201)).andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.id", is(notNullValue())))	
			.andExpect(jsonPath("$.id", is(not(0))))
			.andExpect(jsonPath("$.date", is("2024-05-11")))
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
	@Order(17)
	void createSmallBoxRow4WithUserPedro()throws Exception{
		this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/smallboxes/new")
				.content("{\"date\":\"2024-05-10\",\"ticketNumber\":\"00001-25223\",\"provider\":\"La Comarca S.R.L\",\"inputId\":6,\"ticketTotal\":3000.50}")
				.param("containerId", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(201))
			.andExpect(jsonPath("$.id", is(notNullValue())))	
			.andExpect(jsonPath("$.id", is(not(0))))
			.andExpect(jsonPath("$.date", is("2024-05-10")))
			.andExpect(jsonPath("$.ticketNumber", is("00001-25223")))
			.andExpect(jsonPath("$.provider", is("La Comarca S.R.L")))
			.andExpect(jsonPath("$.description", is("Otros")))
			.andExpect(jsonPath("$.ticketTotal", is(3000.50)))
			.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));
		
	}
	@Test
	@Order(18)
	void createSmallBoxRow5WithUserPedro()throws Exception{
		this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/smallboxes/new")
				.content("{\"date\":\"2022-05-10\",\"ticketNumber\":\"00001-25228\",\"provider\":\"Alimentos Carlos\",\"inputId\":1,\"ticketTotal\":6000}")
				.param("containerId", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(201))
			.andExpect(jsonPath("$.id", is(notNullValue())))	
			.andExpect(jsonPath("$.id", is(not(0))))
			.andExpect(jsonPath("$.date", is("2022-05-10")))
			.andExpect(jsonPath("$.ticketNumber", is("00001-25228")))
			.andExpect(jsonPath("$.provider", is("Alimentos Carlos")))
			.andExpect(jsonPath("$.description", is("Alimento para personas")))
			.andExpect(jsonPath("$.ticketTotal", is(6000)))
			.andExpect(jsonPath("$.containerId", is(Integer.parseInt(container1Id))));
		}
	
	@Test
	@Order(19)
	void editSmallBoxRow3WithUserPedro()throws Exception{
		mvcResult =  this.mockMvc.perform(put("http://localhost:8080/api/v1/small-box/smallboxes/smallBox-update")
				.content("{\"id\":3,\"date\":\"2024-05-11\",\"ticketNumber\":\"00001-23226\",\"provider\":\"Bengala\",\"containerId\":1,\"description\":\"Alimento para animales\",\"ticketTotal\":2500.50}")
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(200)).andDo(MockMvcResultHandlers.print()).andReturn();
		
			String stringResult = mvcResult.getResponse().getContentAsString();
		boolean doesContain = stringResult
				.contains("Se actualizo el comprobante Numero: " + "00001-23226");
		assertTrue(doesContain);
	}
	
	@Test
	@Order(20)
	void completeSmallBoxWithUserPedro()throws Exception{
		mvcResult = this.mockMvc.perform(put("http://localhost:8080/api/v1/small-box/smallboxes/complete")
				.param("containerId", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$[0].id", is(not(0))))
				.andExpect(jsonPath("$[0].id", is(notNullValue())))
				.andExpect(jsonPath("$[0].date", is("2023-04-15")))
				.andExpect(jsonPath("$[0].ticketNumber", is("0002-2223")))
				.andExpect(jsonPath("$[0].provider", is("La Roma")))
				.andExpect(jsonPath("$[0].inputNumber", is("211")))
				.andExpect(jsonPath("$[0].ticketTotal", is(3000.0)))
				.andExpect(jsonPath("$[1].id", is(not(0))))
				.andExpect(jsonPath("$[1].id", is(notNullValue())))
				.andExpect(jsonPath("$[1].date", is("2022-05-10")))
				.andExpect(jsonPath("$[1].ticketNumber", is("00001-25228")))
				.andExpect(jsonPath("$[1].provider", is("Alimentos Carlos")))
				.andExpect(jsonPath("$[1].inputNumber", is("211")))
				.andExpect(jsonPath("$[1].ticketTotal", is(6000.0)))
				.andExpect(jsonPath("$[2].id", is(not(0))))
				.andExpect(jsonPath("$[2].id", is(notNullValue())))
				.andExpect(jsonPath("$[2].subtotal", is(9000)))
				.andExpect(jsonPath("$[2].subtotalTitle", is("SubTotal")))
				.andExpect(jsonPath("$[3].id", is(not(0))))
				.andExpect(jsonPath("$[3].id", is(notNullValue())))
				.andExpect(jsonPath("$[3].date", is("2023-02-10")))
				.andExpect(jsonPath("$[3].ticketNumber", is("0001-2423")))
				.andExpect(jsonPath("$[3].provider", is("Disalar")))
				.andExpect(jsonPath("$[3].inputNumber", is("212")))
				.andExpect(jsonPath("$[3].ticketTotal", is(4000.0)))
				.andExpect(jsonPath("$[4].id", is(not(0))))
				.andExpect(jsonPath("$[4].id", is(notNullValue())))
				.andExpect(jsonPath("$[4].date", is("2024-05-11")))
				.andExpect(jsonPath("$[4].ticketNumber", is("00001-23226")))
				.andExpect(jsonPath("$[4].provider", is("Bengala")))
				.andExpect(jsonPath("$[4].inputNumber", is("212")))
				.andExpect(jsonPath("$[4].ticketTotal", is(2500.50)))
				.andExpect(jsonPath("$[5].id", is(not(0))))
				.andExpect(jsonPath("$[5].id", is(notNullValue())))
				.andExpect(jsonPath("$[5].subtotal", is(6500.5)))
				.andExpect(jsonPath("$[5].subtotalTitle", is("SubTotal")))
				.andExpect(jsonPath("$[6].id", is(not(0))))
				.andExpect(jsonPath("$[6].id", is(notNullValue())))
				.andExpect(jsonPath("$[6].date", is("2024-05-10")))
				.andExpect(jsonPath("$[6].ticketNumber", is("00001-25223")))
				.andExpect(jsonPath("$[6].provider", is("La Comarca S.R.L")))
				.andExpect(jsonPath("$[6].inputNumber", is("219")))
				.andExpect(jsonPath("$[6].ticketTotal", is(3000.50)))
				.andExpect(jsonPath("$[7].id", is(not(0))))
				.andExpect(jsonPath("$[7].id", is(notNullValue())))
				.andExpect(jsonPath("$[7].subtotal", is(3000.5)))
				.andExpect(jsonPath("$[7].subtotalTitle", is("SubTotal"))).andReturn();
				}
	
	@Test
	@Order(21)
	void getSmallBoxTotalWithUserPedro()throws Exception{
		this.mockMvc.perform(get("http://localhost:8080/api/v1/small-box/containers/{containerId}", container1Id)
				.header("Authorization", "Bearer " + userPedrojwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.total", is(18501.0)));
	}
	
	
}
