package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.ChangeStatusRequest;
import org.springframework.stereotype.Component;

@Component
public interface ChangeStatusUsecase {
	void changeStatus(final ChangeStatusRequest request, final Long programId);
}
