package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;

/** 멤버의 활동 상태를 변경한다. */
public interface ChangeActiveStatusUsecase {
	CommandMemberResponse changeStatus(Long memberId, ChangeActiveStatusRequest request);
}
