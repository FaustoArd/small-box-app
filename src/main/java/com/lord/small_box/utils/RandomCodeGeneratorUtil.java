package com.lord.small_box.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomCodeGeneratorUtil {

	public String generateRandomCode(int length,boolean useLetters,boolean useNumbers) {

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

}
