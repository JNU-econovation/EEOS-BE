package com.blackcompany.eeos.notification.application.dto;

import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateNotificationPermissionRequest {

	@NotBlank(message = "푸시 토큰은 필수입니다.")
	private String pushToken;

	@NotNull(message = "푸시 상태값은 필수입니다.")
	private NotificationPermission notificationPermission;
}
