package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.QueryMembersResponse;

public interface GetMembersByActiveStatus {
	QueryMembersResponse execute(String activeStatus);
}
