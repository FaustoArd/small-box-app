package com.lord.small_box;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_MOCKS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class PatternRegexTest {

	@Test
	void patternTest()throws Exception {
		
		Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[tT])(?![@#$%^&+=]).{4,4}$");
		Pattern p2 = Pattern.compile("^(?=.*[tTiI\\s]).{1}([tToOaA0-9\\s]).{3}(.?)*$");
		Matcher m = p2.matcher(" o tqa     I");
		assertTrue(m.matches());
	}
}
