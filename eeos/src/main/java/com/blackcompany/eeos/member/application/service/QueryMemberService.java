package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.member.application.QueryMembersResponse;
import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.dto.converter.QueryMemberResponseConverter;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.usecase.GetMemberByActiveStatus;
import com.blackcompany.eeos.member.application.usecase.GetMembersByActiveStatus;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryMemberService implements GetMembersByActiveStatus, GetMemberByActiveStatus {
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
		MemberEntity member =
				memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

		MemberModel model = entityConverter.from(member);
		return responseConverter.from(model);
	}

	public String getName(final Long memberId) {
		return memberRepository
				.findById(memberId)
				.map(MemberEntity::getName)
				.orElseThrow(NotFoundMemberException::new);
	}

	private List<MemberModel> findMembers() {
		return memberRepository.findMembers().stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}

	private List<MemberModel> findMembersByStatus(ActiveStatus activeStatus) {
		return memberRepository.findMembersByActiveStatus(activeStatus).stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}
}
