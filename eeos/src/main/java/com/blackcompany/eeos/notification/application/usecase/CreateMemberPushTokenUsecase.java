package com.blackcompany.eeos.notification.application.usecase;

import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;

public interface CreateMemberPushTokenUsecase {
	void create(Long memberId, CreateMemberPushTokenRequest request);
}
