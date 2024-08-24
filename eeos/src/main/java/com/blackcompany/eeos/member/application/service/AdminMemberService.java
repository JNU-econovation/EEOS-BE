package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.auth.persistence.OAuthMemberRepository;
import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;
import com.blackcompany.eeos.member.application.dto.converter.CommandMemberResponseConverter;
import com.blackcompany.eeos.member.application.exception.DeniedMemberEditException;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.usecase.ChangeActiveStatusUsecase;
import com.blackcompany.eeos.member.event.DeletedMemberEvent;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMemberService implements ChangeActiveStatusUsecase {
	private final MemberRepository memberRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final MemberEntityConverter memberConverter;
	private final CommandMemberResponseConverter responseConverter;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	@Override
	public CommandMemberResponse changeActiveStatus(
			final Long adminMemberId, Long memberId, final ChangeActiveStatusRequest request) {
		validateAdminPermission(adminMemberId);

		MemberModel model = findMember(memberId);
		MemberEntity updateMember = updateActiveStatus(model, request.getActiveStatus());

		return responseConverter.from(
				updateMember.getName(), updateMember.getActiveStatus().getStatus());
	}

	@Override
	@Transactional
	public void delete(final Long adminMemberId, final Long memberId) {
		validateAdminPermission(adminMemberId);

		MemberModel member = findMember(memberId);
		memberRepository.deleteById(member.getId());
		oAuthMemberRepository.deleteById(member.getId());

		applicationEventPublisher.publishEvent(DeletedMemberEvent.of(memberId));
	}

	private void validateAdminPermission(Long memberId) {
		MemberModel member = findMember(memberId);

		if (member.isAdmin()) {
			return;
		}
		throw new DeniedMemberEditException(memberId);
	}

	private MemberModel findMember(final Long memberId) {
		return memberRepository
				.findById(memberId)
				.map(memberConverter::from)
				.orElseThrow(NotFoundMemberException::new);
	}

	private MemberEntity updateActiveStatus(final MemberModel model, final String status) {
		MemberModel memberModel = model.updateActiveStatus(status);
		return memberRepository.save(memberConverter.toEntity(memberModel));
	}
}
