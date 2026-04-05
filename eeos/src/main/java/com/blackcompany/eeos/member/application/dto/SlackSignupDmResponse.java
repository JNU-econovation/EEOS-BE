package com.blackcompany.eeos.member.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackSignupDmResponse {

	@Schema(description = "DM 발송 대상 총 인원 수", example = "12")
	private int totalCount;

	@Schema(description = "DM 발송 성공 인원 수", example = "11")
	private int successCount;

	@Schema(description = "DM 발송 실패 인원 수", example = "1")
	private int failCount;
}
