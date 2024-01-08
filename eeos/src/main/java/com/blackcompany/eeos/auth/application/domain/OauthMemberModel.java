package com.blackcompany.eeos.auth.application.domain;

import com.blackcompany.eeos.auth.application.exception.InvalidNameFormatException;
import com.blackcompany.eeos.auth.application.support.ValidatorNameFormat;
import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OauthMemberModel implements AbstractModel {
	private String oauthId;
	private String name;
	private OauthServerType oauthServerType;

	public void validateNameFormat() {
		if (!ValidatorNameFormat.isSatisfy(name)) {
			throw new InvalidNameFormatException(name);
		}
	}
}
