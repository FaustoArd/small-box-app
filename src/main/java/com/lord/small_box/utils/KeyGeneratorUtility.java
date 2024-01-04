package com.lord.small_box.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtility {

	
	public static KeyPair generateRsaKey() {
		KeyPair keyPair;
		
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}catch(NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return keyPair;
	}
}


