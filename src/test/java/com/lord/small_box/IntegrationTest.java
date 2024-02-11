package com.lord.small_box;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mapstruct.ap.internal.model.Constructor;
import org.mapstruct.ap.internal.model.Mapper;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.LoginResponseDto;
import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;
import com.lord.small_box.models.SmallBoxType;
import com.lord.small_box.repositories.AuthorityRepository;
import com.lord.small_box.repositories.SmallBoxTypeRepository;
import com.lord.small_box.services.AuthorizationService;
import com.nimbusds.jose.JWSObject;

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
	private MockMvc mockMvc;

	private MvcResult mvcResult;

	private String jwtToken;
	
	private String userPedrojwtToken;
	

	private Long lagunasId;

	private Long yanezId;

	private Long sitCalleId;

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
		userDto.setPassword("123");
		authorizationService.register(userDto, "ADMIN");
		
		SmallBoxType chica = SmallBoxType.builder()
				.smallBoxType("CHICA").build();
				SmallBoxType savedCHica =  smallBoxTypeRepository.save(chica);
		
		SmallBoxType especial = SmallBoxType.builder().smallBoxType("ESPECIAL")
				.build();
		SmallBoxType savedEspecial = smallBoxTypeRepository.save(especial);
	}

	@Test
	@Order(1)
	void loginAsAdmin() throws Exception {

		mvcResult = mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/authorization/login")
						.content("{\"username\":\"car\",\"password\":\"123\"}").contentType(MediaType.APPLICATION_JSON))
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
				"{\"name\":\"Pedro\",\"lastname\":\"Mozart\",\"username\":\"pedro29\",\"email\":\"car_moz@gmail.com\",\"password\":\"pass\"}")
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
						.content("{\"username\":\"pedro29\",\"password\":\"pass\"}").contentType(MediaType.APPLICATION_JSON))
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
	 mvcResult = 	this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/containers/")
			 .content("{\"smallBoxType\":\"CHICA\",\"organization\":\"1\"}")
			 .header("Authorization", "Bearer " + userPedrojwtToken)
			 .contentType(MediaType.APPLICATION_JSON))
			 .andExpect(status().is(201)).andDo(MockMvcResultHandlers.print())
			 .andExpect(jsonPath("$.id", is(notNullValue())))
			 .andExpect(jsonPath("$.id", is(not(0))))
			 .andExpect(jsonPath("$.smallBoxType", is("CHICA")))
			 .andExpect(jsonPath("$.organization", is("Dir  de Logistica")))
			 .andExpect(jsonPath("$.responsible", is("Fabian Yanez")))
			 .andExpect(jsonPath("$.smalBoxDate[0]", is(2024)))
			.andReturn();
	}
}
