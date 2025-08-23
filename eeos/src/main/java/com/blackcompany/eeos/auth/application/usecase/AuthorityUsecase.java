package com.blackcompany.eeos.auth.application.usecase;

public interface AuthorityUsecase {

    void changeRole(Long memberId, String from, String to);

}
