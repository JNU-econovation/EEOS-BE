package com.blackcompany.eeos.auth.application.model;

import com.blackcompany.eeos.auth.persistence.AccountEntity;
import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityConverter
		implements AbstractEntityConverter<AccountEntity, AccountModel> {

	@Override
	public AccountModel from(AccountEntity source) {
		return AccountModel.builder()
				.id(source.getId())
				.loginId(source.getLoginId())
				.password(source.getPassWd())
				.memberId(source.getMemberId())
				.build();
	}

	@Override
	public AccountEntity toEntity(AccountModel source) {
		return AccountEntity.builder()
				.loginId(source.getLoginId())
				.passWd(source.getPassword())
				.memberId(source.getMemberId())
				.build();
	}
}
