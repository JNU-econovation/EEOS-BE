package com.blackcompany.eeos.auth.application.support;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptImpl implements EncryptHelper {

	public String encrypt(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public boolean isMatch(String password, String encryptedPassword) {
		return BCrypt.checkpw(password, encryptedPassword);
	}
}
