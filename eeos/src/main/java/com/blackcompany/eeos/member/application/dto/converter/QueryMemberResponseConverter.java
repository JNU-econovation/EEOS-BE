package com.blackcompany.eeos.member.application.dto.converter;

import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.dto.QueryMemberResponse.Members;
import com.blackcompany.eeos.member.application.model.MemberModel;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QueryMemberResponseConverter {
	public QueryMemberResponse from(List<Members> members) {
		return QueryMemberResponse.builder().members(members).build();
	}

	public QueryMemberResponse.Members from(MemberModel source) {
		return QueryMemberResponse.Members.builder()
				.memberId(source.getId())
				.name(source.getName())
				.activeStatus(source.getActiveStatus().getStatus())
				.build();
	}
}
