package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusResponse;

public interface GetAttendStatusUsecase {
	ChangeAttendStatusResponse getStatus(final Long memberId, final Long programId);
}
