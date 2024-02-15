package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.TargetMember;
import com.blackcompany.eeos.common.application.model.MemberIdModel;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class SelectTargetService {
	private final MemberRepository memberRepository;
	private final MemberEntityConverter memberEntityConverter;

	/**
	 * 대상자 멤버들로 멤버를 조회합니다.
	 *
	 * @param members 대상자 멤버 리스트
	 */
	protected <T extends TargetMember> List<MemberModel> findMembers(final List<T> members) {
		List<Long> requestMemberIds =
				members.stream().map(TargetMember::getMemberId).collect(Collectors.toList());

		List<MemberModel> findMembers = findMembersByIds(requestMemberIds);
		validateAllFind(requestMemberIds, findMembers);
		return findMembers;
	}

	/**
	 * 요청한 member Ids로 멤버들을 조회합니다.
	 *
	 * @param requestMemberIds 멤버 id 리스트
	 */
	protected List<MemberModel> findMembersByIds(List<Long> requestMemberIds) {
		return memberRepository.findMembersByIds(requestMemberIds).stream()
				.map(memberEntityConverter::from)
				.collect(Collectors.toList());
	}

	/**
	 * 요청한 멤버 Ids로 모든 멤버들을 찾았는지 검사합니다.
	 *
	 * @param requestMemberIds 요청한 member id 리스트
	 * @param findMembers 요청한 member is로 찾은 member 리스트
	 */
	protected <T extends MemberIdModel> void validateAllFind(
			List<Long> requestMemberIds, List<T> findMembers) {
		if (requestMemberIds.size() == findMembers.size()) {
			return;
		}
		throw new NotFoundMemberException();
	}
}
