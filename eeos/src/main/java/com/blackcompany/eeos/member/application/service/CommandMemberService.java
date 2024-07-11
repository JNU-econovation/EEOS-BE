package com.blackcompany.eeos.member.application.service;

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
public class CommandMemberService implements ChangeActiveStatusUsecase {
	private final MemberRepository memberRepository;
	private final MemberEntityConverter memberConverter;
	private final CommandMemberResponseConverter responseConverter;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	@Override
	public CommandMemberResponse changeStatus(
			final Long memberId, final ChangeActiveStatusRequest request) {
		MemberModel model =
				memberRepository
						.findById(memberId)
						.map(memberConverter::from)
						.orElseThrow(NotFoundMemberException::new);

		MemberEntity updatedMember = updateActiveStatus(model, request.getActiveStatus());

		return responseConverter.from(
				updatedMember.getName(), updatedMember.getActiveStatus().getStatus());
	}

	@Transactional
	@Override
	public CommandMemberResponse adminChangeStatus(
			final Long adminMemberId, Long memberId, final ChangeActiveStatusRequest request) {
		isAdmin(adminMemberId);
		MemberModel model =
				memberRepository
						.findById(memberId)
						.map(memberConverter::from)
						.orElseThrow(NotFoundMemberException::new);

		MemberEntity updateMember = updateActiveStatus(model, request.getActiveStatus());

		return responseConverter.from(
				updateMember.getName(), updateMember.getActiveStatus().getStatus());
	}

	private MemberEntity updateActiveStatus(final MemberModel model, final String status) {
		MemberModel memberModel = model.updateActiveStatus(status);
		return memberRepository.save(memberConverter.toEntity(memberModel));
	}

	private MemberModel findMember(final Long memberId) {
		return memberRepository
				.findById(memberId)
				.map(memberConverter::from)
				.orElseThrow(NotFoundMemberException::new);
	}

	public void isAdmin(Long memberId) {
		if (memberId == 0) {
			return;
		}
		throw new DeniedMemberEditException(memberId);
	}

	@Override
	@Transactional
	public void delete(final Long adminMemberId, final Long memberId) {
		MemberModel member = findMember(memberId);
		isAdmin(adminMemberId);

		memberRepository.deleteById(member.getId());
		applicationEventPublisher.publishEvent(DeletedMemberEvent.of(memberId));
	}
}
