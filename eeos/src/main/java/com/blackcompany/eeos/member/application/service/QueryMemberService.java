package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.dto.QueryMembersResponse;
import com.blackcompany.eeos.member.application.dto.converter.QueryMemberResponseConverter;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.application.usecase.GetMemberByActiveStatus;
import com.blackcompany.eeos.member.application.usecase.GetMembersByActiveStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryMemberService implements GetMembersByActiveStatus, GetMemberByActiveStatus {
	private final MemberEntityConverter entityConverter;
	private final MemberRepository memberRepository;
	private final QueryMemberResponseConverter responseConverter;
	private final MemberEntityConverter memberEntityConverter;

	@Override
	public QueryMembersResponse execute(final String activeStatus) {
		ActiveStatus status = ActiveStatus.find(activeStatus);

		if (status.isAll()) {
			List<MemberModel> models = findMembers();
			return responseConverter.from(models);
		}

		List<MemberModel> models = findMembersByStatus(status);
		return responseConverter.from(models);
	}

	@Override
	public QueryMemberResponse execute(Long memberId) {
		MemberModel model = memberRepository.findById(memberId);

		return responseConverter.from(model);
	}

	public MemberModel findMember(Long memberId) {
		return memberRepository.findById(memberId);
	}

	public String getName(final Long memberId) {
		return memberRepository.findById(memberId).getName();
	}

	private List<MemberModel> findMembers() {
		return memberRepository.findMembers().stream()
				.filter(m -> !m.isAdmin())
				.toList();
	}

	private List<MemberModel> findMembersByStatus(ActiveStatus activeStatus) {
		return memberRepository.findMembersByActiveStatus(activeStatus).stream()
				.filter(m -> !m.isAdmin())
				.toList();
	}
}
