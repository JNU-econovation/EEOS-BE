package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;

public interface GetAttendStatusUsecase {
	ChangeAttendStatusResponse getStatus(final Long memberId, final Long programId);
}
