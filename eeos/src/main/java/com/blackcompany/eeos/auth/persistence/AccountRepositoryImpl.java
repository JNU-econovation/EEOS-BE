package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.auth.application.exception.NotFoundAccountException;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    @Override
    public AccountModel findByLoginId(String loginId) {
        AccountEntity entity = jpaRepository.findByLoginId(loginId)
                .orElseThrow(NotFoundAccountException::new); // TODO: 이 예외는 application 계층의 예외이므로, persistence 영역의 예외로 변경
        return AccountModel.builder()
                .id(entity.getId())
                .loginId(entity.getLoginId())
                .password(entity.getPassWd())
                .build();
    }
}
