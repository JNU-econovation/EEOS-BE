package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;

public interface ChangeActiveStatusUsecase {
	CommandMemberResponse execute(Long memberId, ChangeActiveStatusRequest request);
}
