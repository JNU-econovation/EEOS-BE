package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.Getter;

@Getter
public record AttendSummaryInfoResponse(
        int attendCount,
        int lateCount,
        int absentCount,
        int penaltyPoint
)
implements AbstractResponseDto {
}
