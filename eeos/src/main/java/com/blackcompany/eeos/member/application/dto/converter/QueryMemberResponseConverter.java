package com.blackcompany.eeos.member.application.dto.converter;

import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.dto.QueryMembersResponse;
import com.blackcompany.eeos.member.application.model.MemberModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class QueryMemberResponseConverter {
	public QueryMemberResponse from(MemberModel source) {
		return QueryMemberResponse.builder()
				.memberId(source.getId())
				.name(source.getName())
				.activeStatus(source.getActiveStatus().getStatus())
				.build();
	}

	public QueryMembersResponse from(List<MemberModel> sources) {
		return QueryMembersResponse.builder()
				.members(sources.stream().map(this::from).collect(Collectors.toList()))
				.build();
	}
}
