package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.QueryMembersResponse;

public interface GetMembersByActiveStatus {
	QueryMembersResponse execute(String activeStatus);
}
