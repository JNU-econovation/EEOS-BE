package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.ChangeStatusRequest;

public interface ChangeAttendStatusUsecase {
	void changeStatus(final ChangeStatusRequest request, final Long programId);
}
