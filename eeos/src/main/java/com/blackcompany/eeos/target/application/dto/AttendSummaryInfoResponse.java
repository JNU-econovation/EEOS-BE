package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public record AttendSummaryInfoResponse(
		Long memberId, Long attendCount, Long absentCount, Long penaltyPoint)
		implements AbstractResponseDto {}
