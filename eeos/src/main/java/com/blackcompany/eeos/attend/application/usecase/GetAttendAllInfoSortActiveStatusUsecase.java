package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;

public interface GetAttendAllInfoSortActiveStatusUsecase {
	QueryAttendStatusResponse execute(final Long programId, final String activeStatus);
}
