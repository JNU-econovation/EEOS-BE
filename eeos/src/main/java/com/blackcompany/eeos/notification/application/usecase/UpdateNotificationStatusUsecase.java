package com.blackcompany.eeos.notification.application.usecase;

import com.blackcompany.eeos.notification.application.dto.UpdatePushStatusRequest;

public interface UpdateNotificationStatusUsecase {
	void updateStatus(Long memberId, UpdatePushStatusRequest request);
}
