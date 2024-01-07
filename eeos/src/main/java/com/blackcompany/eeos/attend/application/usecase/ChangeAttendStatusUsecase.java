package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.program.application.dto.ChangeAttendStatusRequest;

public interface ChangeAttendStatusUsecase {
	void changeStatus(final ChangeAttendStatusRequest request, final Long programId);
}
