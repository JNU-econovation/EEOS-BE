package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.QueryTargetInfoResponse;

public interface GetTargetInfoUsecase {
	QueryTargetInfoResponse getTargetInfo(Long memberId);
}
