package com.blackcompany.eeos.notification.application.model;

import java.util.Arrays;

import com.blackcompany.eeos.notification.application.exception.NotFoundNotificationProviderException;

import lombok.Getter;

@Getter
public enum NotificationProvider {
	FCM("fcm"),
	ETC("etc");

	private final String provider;

	NotificationProvider(String provider) {
		this.provider = provider;
	}

	public static NotificationProvider find(String name){
		return Arrays.stream(NotificationProvider.values())
			.filter(provider -> provider.name().equalsIgnoreCase(name))
			.findFirst()
			.orElseThrow(() -> new NotFoundNotificationProviderException(name));
	}

}
