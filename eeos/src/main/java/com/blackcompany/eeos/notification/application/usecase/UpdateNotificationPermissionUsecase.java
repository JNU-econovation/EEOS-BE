package com.blackcompany.eeos.notification.application.usecase;

import com.blackcompany.eeos.notification.application.dto.UpdateNotificationPermissionRequest;

public interface UpdateNotificationPermissionUsecase {
	void updateNotificationPermission(Long memberId, UpdateNotificationPermissionRequest request);
}
