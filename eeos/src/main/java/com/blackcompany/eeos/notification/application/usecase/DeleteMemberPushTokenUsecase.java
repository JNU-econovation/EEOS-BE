package com.blackcompany.eeos.notification.application.usecase;

import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;

public interface DeleteMemberPushTokenUsecase {
	void delete(Long memberId, DeleteMemberPushTokenRequest request);
}
