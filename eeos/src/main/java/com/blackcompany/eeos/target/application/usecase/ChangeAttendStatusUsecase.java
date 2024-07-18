package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;

public interface ChangeAttendStatusUsecase {
	ChangeAttendStatusResponse changeStatus(
			final Long memberId, final Long programId);
}
