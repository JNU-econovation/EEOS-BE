package com.blackcompany.eeos.auth.application.support;

public interface EncryptHelper {

	String encrypt(String password);

	boolean isMatch(String password, String encryptedPassword);
}
