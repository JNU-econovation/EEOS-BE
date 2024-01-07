package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.QueryAccessRightResponse;

public interface GetAccessRightUsecase {
	QueryAccessRightResponse getAccessRight(Long memberId, Long programId);
}
