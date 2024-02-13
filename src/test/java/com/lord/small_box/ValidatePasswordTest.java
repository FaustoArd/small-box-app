package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.services.AuthorizationService;

@SpringBootTest
public class ValidatePasswordTest {

	
	@Autowired
	private AuthorizationService authorizationService;
	
	
	@Test
	void validatePassword() {
		String password = "Pta1919az35&";
		
		boolean result = authorizationService.validatePassword(password);
		assertTrue(result);
	}
}
