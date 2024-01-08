package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.QueryAttendActiveStatusResponse;

public interface GetAttendAllInfoSortActiveStatusUsecase {
	QueryAttendActiveStatusResponse execute(final Long programId, final String activeStatus);
}
