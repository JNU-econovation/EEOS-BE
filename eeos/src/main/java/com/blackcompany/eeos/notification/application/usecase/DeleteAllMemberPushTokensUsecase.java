package com.blackcompany.eeos.notification.application.usecase;

import org.springframework.stereotype.Component;

@Component
public interface DeleteAllMemberPushTokensUsecase {
	void deleteAllMemberPushTokens(Long memberId);
}
