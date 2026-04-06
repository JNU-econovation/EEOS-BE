package com.blackcompany.eeos.auth.application.dto.response;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse implements AbstractResponseDto {
	private String accessToken;
	private Long accessExpiredTime;
	private String refreshToken;
}
