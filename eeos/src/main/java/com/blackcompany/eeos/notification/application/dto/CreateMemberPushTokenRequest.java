package com.blackcompany.eeos.notification.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateMemberPushTokenRequest {
	@NotNull private String pushToken;
	@NotNull private String provider;
}
