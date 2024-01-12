package com.lord.small_box;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lord.small_box.dtos.AppUserRegistrationDto;
import com.lord.small_box.dtos.LoginResponseDto;
import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;
import com.lord.small_box.repositories.AuthorityRepository;
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
	private MockMvc mockMvc;

	private MvcResult mvcResult;

	private String jwtToken;
	
	private Long lagunasId;
	
	private Long sitCalleId;

	@BeforeAll
	void setup() {
		Authority admin = new Authority();
		admin.setAuthority(AuthorityName.ADMIN);
		Authority user = new Authority();
		user.setAuthority(AuthorityName.USER);

		authorityRepository.save(admin);
		authorityRepository.save(user);

		AppUserRegistrationDto userDto = new AppUserRegistrationDto();
		userDto.setName("Carlos");
		userDto.setLastname("Marroin");
		userDto.setEmail("car@mail.com");
		userDto.setUsername("car");
		userDto.setPassword("123");
		authorizationService.register(userDto, "ADMIN");
	}

	@Test
	@Order(1)
	void loginAsAdmin() throws Exception {

		mvcResult = mockMvc
				.perform(post("http://localhost:8080/api/v1/small-box/authorization/login")
						.content("{\"username\":\"car\",\"password\":\"123\"}")
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
	void registerUser() throws Exception {
		  this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/registration/register").content(
				"{\"name\":\"Pedro\",\"lastname\":\"Mozart\",\"username\":\"pedro29\",\"email\":\"car_moz@gmail.com\",\"password\":\"pass\"}")
				.header("Authorization", "Bearer " + jwtToken)
				.param("authority", "USER")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
				.andExpect(jsonPath(("$.name"), is("Pedro")))
				.andExpect(jsonPath("$.username", is("pedro29")))
				.andExpect(jsonPath("$.password", is(nullValue())));
		
	}
	
	@Test
	@Order(3)
	void createResponsible()throws Exception{
		mvcResult =	this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/organization/new-responsible")
				.content("{\"name\":\"Analia\",\"lastname\":\"Lagunas\"}")
				.header("Authorization",  "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
		.andExpect(jsonPath("$.id", is(notNullValue())))
		.andExpect(jsonPath("$.name", is("Analia")))
		.andExpect(jsonPath("$.lastname", is("Lagunas"))).andReturn();
		String[] list = mvcResult.getResponse().getContentAsString().split("\"");
		String s = Character.toString(list[2].charAt(1));
		lagunasId = Long.parseLong(s);
		
					
	}
	@Test
	@Order(4)
	void createOrganization()throws Exception{
		mvcResult = this.mockMvc.perform(post("http://localhost:8080/api/v1/small-box/organization/new-organization")
				.content("{\"organizationName\":\"Dir  de personas en situacion de calle\",\"organizationNumber\":5,\"responsibleId\":1,\"maxRotation\":12,\"maxAmount\":45000}")
				.header("Authorization",  "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.id", is(notNullValue())))
				.andExpect(jsonPath("$.organizationName", is("Dir  de personas en situacion de calle")))
				.andExpect(jsonPath("$.maxRotation", is(12)))
				.andExpect(jsonPath("$.maxAmount", is(45000)))
				.andExpect(jsonPath("$.responsible",is("Analia Lagunas"))).andReturn();
	}
	
}
