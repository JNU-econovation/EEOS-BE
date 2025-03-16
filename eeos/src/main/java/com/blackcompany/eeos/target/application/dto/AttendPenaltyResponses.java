package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.util.List;

public record AttendPenaltyResponses(List<AttendPenaltyResponse> responses)
		implements AbstractResponseDto {}
