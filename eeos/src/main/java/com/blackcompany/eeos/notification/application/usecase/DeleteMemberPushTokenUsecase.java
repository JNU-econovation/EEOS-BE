package com.blackcompany.eeos.notification.application.usecase;

import org.springframework.stereotype.Component;

import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;

@Component
public interface DeleteMemberPushTokenUsecase {
	void delete(Long memberId, DeleteMemberPushTokenRequest request);
}
