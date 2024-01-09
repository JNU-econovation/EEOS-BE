package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.QueryAttendActiveStatusResponse;

/** 프로그램에 관련 없음 대상자도 활동 상태를 기준으로 조회한다. */
public interface GetAttendAllInfoSortActiveStatusUsecase {
	QueryAttendActiveStatusResponse getAttendInfo(final Long programId, final String activeStatus);
}
