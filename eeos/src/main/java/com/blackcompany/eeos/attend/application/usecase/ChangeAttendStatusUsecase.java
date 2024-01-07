package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusResponse;

public interface ChangeAttendStatusUsecase {
	ChangeAttendStatusResponse changeStatus(
			final Long memberId, final ChangeAttendStatusRequest request, final Long programId);
}
