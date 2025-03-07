package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.Builder;

@Builder
public record AttendPenaltyResponse(
    Long memberId,
    String name,
    Long penaltyPoint,
    Long rank
) implements AbstractResponseDto {
}
