package com.blackcompany.eeos.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;

@Component
public interface CreateMemberPushTokenUsecase {
	void create(Long memberId, CreateMemberPushTokenRequest request);

}
