package com.blackcompany.eeos.auth.application.usecase;

public interface LogOutUsecase {
	void logOut(String token, final Long memberId);
}
