package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.model.AccountModel;

public interface AccountRepository {

	AccountModel findByLoginId(String loginId);

	AccountModel save(AccountModel model);

	boolean existsByLoginId(String loginId);

	boolean existsByMemberId(Long memberId);
}
