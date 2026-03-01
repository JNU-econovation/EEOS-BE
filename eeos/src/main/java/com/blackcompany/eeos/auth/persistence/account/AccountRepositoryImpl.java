package com.blackcompany.eeos.auth.persistence.account;

import com.blackcompany.eeos.auth.application.exception.NotFoundAccountException;
import com.blackcompany.eeos.auth.application.model.AccountEntityConverter;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

	private final AccountJpaRepository jpaRepository;
	private final AccountEntityConverter converter;

	@Override
	public AccountModel findByLoginId(String loginId) {
		AccountEntity entity =
				jpaRepository
						.findByLoginId(loginId)
						.orElseThrow(
								NotFoundAccountException
										::new); // TODO: 이 예외는 application 계층의 예외이므로, persistence 영역의 예외로 변경
		return converter.from(entity);
	}

	@Override
	public AccountModel save(AccountModel model) {
		AccountEntity entity = converter.toEntity(model);
		AccountEntity saved = jpaRepository.save(entity);
		return converter.from(saved);
	}

	@Override
	public boolean existsByLoginId(String loginId) {
		return jpaRepository.existsByLoginId(loginId);
	}
}
