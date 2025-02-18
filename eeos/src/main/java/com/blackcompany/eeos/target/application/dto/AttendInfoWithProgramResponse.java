package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.Builder;

@Builder
public record AttendInfoWithProgramResponse(
		Long programId, String title, String programStatus, String attendStatus)
		implements AbstractResponseDto {}
