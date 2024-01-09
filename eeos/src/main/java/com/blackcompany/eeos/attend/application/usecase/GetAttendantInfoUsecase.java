package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;
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
}
