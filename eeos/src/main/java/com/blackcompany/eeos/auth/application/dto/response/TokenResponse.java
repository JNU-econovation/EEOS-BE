package com.blackcompany.eeos.auth.application.dto.response;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenResponse implements AbstractResponseDto {
	private String accessToken;
	private Long accessExpiredTime;
}
