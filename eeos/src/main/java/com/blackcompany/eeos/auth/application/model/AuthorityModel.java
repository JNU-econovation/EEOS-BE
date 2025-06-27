package com.blackcompany.eeos.auth.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class AuthorityModel implements AbstractModel {

	private final Long id;
	private final Long memberId;
	private final String name; // TODO: ROLE enum 으로 변경
}
