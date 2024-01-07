package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusRequest;

public interface ChangeAttendStatusUsecase {
	void changeStatus(
			final Long memberId, final ChangeAttendStatusRequest request, final Long programId);
}
