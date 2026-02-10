package com.blackcompany.eeos.notification.application.model;

import com.blackcompany.eeos.notification.application.exception.NotFoundNotificationPermissionException;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum NotificationPermission {
	ON("on"),
	OFF("off"),
	;

	private final String notificationPermission;

	NotificationPermission(String notificationPermission) {
		this.notificationPermission = notificationPermission;
	}

	public String getNotificationPermission() {
		return notificationPermission;
	}

	public static NotificationPermission find(String notificationPermission) {
		return Arrays.stream(values())
				.filter(
					permission ->
						permission.getNotificationPermission().equals(notificationPermission))
				.findAny()
				.orElseThrow(() -> new NotFoundNotificationPermissionException(notificationPermission));
	}

	@JsonCreator
	public static NotificationPermission from(String value) {
		if(value == null) {
			throw new NotFoundNotificationPermissionException("null");
		}
		return find(value.toLowerCase());
	}
}
