package com.blackcompany.eeos.notification.application.usecase;

import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;
import org.springframework.stereotype.Component;

@Component
public interface CreateMemberPushTokenUsecase {
	void create(Long memberId, CreateMemberPushTokenRequest request);
}
