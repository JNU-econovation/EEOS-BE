package com.blackcompany.eeos.notification.application.model;

import java.util.Arrays;
import java.util.Locale;

import com.blackcompany.eeos.notification.application.exception.NotFoundPushStatusException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum PushStatus {
	ON("on"),
	OFF("off"),
	;

	private final String status;

	PushStatus(String status) {
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public static PushStatus find(String status){
		return Arrays.stream(values())
			.filter(pushStatus -> pushStatus.getStatus().equals(status))
			.findAny()
			.orElseThrow(() -> new NotFoundPushStatusException(status));
	}

	@JsonCreator
	public static PushStatus from(String value){
		return find(value.toLowerCase());
	}
}
