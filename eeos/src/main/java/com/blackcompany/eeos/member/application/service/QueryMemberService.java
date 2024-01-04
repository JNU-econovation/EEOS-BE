package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.dto.QueryMemberResponse.Members;
import com.blackcompany.eeos.member.application.dto.converter.QueryMemberResponseConverter;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.usecase.GetMembersByActiveStatus;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryMemberService implements GetMembersByActiveStatus {
	private final MemberEntityConverter entityConverter;
	private final MemberRepository memberRepository;
	private final QueryMemberResponseConverter responseConverter;

	@Deprecated
	public List<MemberModel> findAllMember() {
		return memberRepository.findAll().stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}

	@Override
	public QueryMemberResponse execute(String activeStatus) {
		List<Members> members =
				memberRepository.findMembersByActiveStatus(activeStatus).stream()
						.map(entityConverter::from)
						.map(responseConverter::from)
						.collect(Collectors.toList());

		return responseConverter.from(members);
	}
}
