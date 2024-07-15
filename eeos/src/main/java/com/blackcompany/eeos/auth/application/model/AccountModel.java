package com.blackcompany.eeos.auth.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class AccountModel implements AbstractModel {

	private Long id;
	private Long memberId;
	private String loginId;
	private String password;
}
