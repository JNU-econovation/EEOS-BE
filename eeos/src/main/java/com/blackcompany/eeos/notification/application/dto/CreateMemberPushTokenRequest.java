package com.blackcompany.eeos.notification.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateMemberPushTokenRequest {
	@NotBlank private String pushToken;
	@NotBlank private String provider;
}
