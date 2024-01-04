package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;

public interface GetMemberByActiveStatus {
	QueryMemberResponse execute(Long memberId);
}
