package com.blackcompany.eeos.auth.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfoRequest {
	@NotBlank(message = "이름은 필수 입력값입니다")
	private String name;

	@NotNull(message = "기수는 필수 입력값입니다")
	@Min(value = 11, message = "기수는 11 이상이어야 합니다")
	private Integer generation;

	@NotBlank(message = "활동 상태는 필수 입력값입니다")
	private String activeStatus;
}
