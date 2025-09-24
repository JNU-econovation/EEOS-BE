package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.common.presentation.response.PageResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoWithProgramResponse;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyRankingResponse;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyResponse;
import com.blackcompany.eeos.target.application.dto.AttendStatisticsResponse.MemberStatistics;
import com.blackcompany.eeos.target.application.dto.AttendSummaryInfoResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import java.util.List;

public interface GetAttendantInfoUsecase {
	List<AttendInfoResponse> findAttendInfo(final Long programId);

	/**
	 * 프로그램의 관련 있음 대상자만 참석 상태를 기준으로 조회한다.
	 *
	 * @param programId
	 * @param attendStatus
	 * @return
	 */
	QueryAttendStatusResponse findAttendInfo(final Long programId, final String attendStatus);

	QueryAttendStatusResponse findFireFingerMembers(Long programId);

	PageResponse<AttendInfoWithProgramResponse> findMyAttendInfo(
			final int page, final int size, final long startDate, final long endDate);

	PageResponse<AttendPenaltyResponse> getPenaltyInfos(
			int page, int size, String sortType, Long startDate, Long endDate);

	AttendSummaryInfoResponse getMyAttendSummary(Long startDate, Long endDate);

	AttendPenaltyRankingResponse getMyPenaltyRanking(int rankOffset);

	PageResponse<MemberStatistics> getStatistics(
			int page, int size, String activeStatus, Long startDate, Long endDate);
}
