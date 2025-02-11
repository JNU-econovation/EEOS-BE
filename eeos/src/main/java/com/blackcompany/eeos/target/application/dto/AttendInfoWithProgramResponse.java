package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public record AttendInfoWithProgramResponse(
        Long programId,
        String programStatus,
        String programName,
        String attendStatus
) implements AbstractResponseDto {
}
