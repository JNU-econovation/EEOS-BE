package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;

public interface ChangeAttendStatusUsecase {
	ChangeAttendStatusResponse changeStatus(
			final Long memberId, final ChangeAttendStatusRequest request, final Long programId);
}
