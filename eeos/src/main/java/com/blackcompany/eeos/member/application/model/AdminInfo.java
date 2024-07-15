package com.blackcompany.eeos.member.application.model;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.support.BcryptImpl;
import com.blackcompany.eeos.auth.application.support.EncryptHelper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AdminInfo {

	private static final EncryptHelper encryptHelper = new BcryptImpl();
	private final OauthServerType oauthServerType = OauthServerType.EEOS;
	private final ActiveStatus activeStatus = ActiveStatus.OB;
	private final String name;
	private final String loginId;
	private final String password;

	public AdminInfo(
			@Value("${eeos.admin.name}") String name,
			@Value("${eeos.admin.loginId}") String loginId,
			@Value("${eeos.admin.password}") String password) {
		this.name = name;
		this.loginId = loginId;
		this.password = encryptHelper.encrypt(password);
	}
}
