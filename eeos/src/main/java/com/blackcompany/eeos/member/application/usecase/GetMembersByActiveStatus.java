package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;

public interface GetMembersByActiveStatus {
	QueryMemberResponse execute(String activeStatus);
}
