package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.model.AccountModel;

public interface AccountRepository {

    AccountModel findByLoginId(String loginId);

}
