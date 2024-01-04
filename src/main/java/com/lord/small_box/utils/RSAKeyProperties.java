package com.lord.small_box.utils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

@Component
public class RSAKeyProperties {
	
	private RSAPublicKey publicKey;
	
	private RSAPrivateKey privateKey;
	
	
	
	public RSAKeyProperties() {
		KeyPair pair = KeyGeneratorUtility.generateRsaKey();
		this.publicKey = (RSAPublicKey) pair.getPublic();
		this.privateKey = (RSAPrivateKey) pair.getPrivate();
	}
	
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(RSAPublicKey publicKey) {
		this.publicKey = publicKey;
	}

}
