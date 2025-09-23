package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.util.List;

public record AttendStatisticsResponse(List<MemberStatistics> members)
		implements AbstractResponseDto {

	public record MemberStatistics(
			Long id,
			String name,
			String activeStatus,
			int lateCount,
			int absentCount,
			int penaltyPoint) {}
}
