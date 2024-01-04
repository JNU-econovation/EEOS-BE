package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;
import com.blackcompany.eeos.member.application.dto.converter.CommandMemberResponseConverter;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.usecase.ChangeActiveStatusUsecase;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommandMemberService implements ChangeActiveStatusUsecase {
	private final MemberRepository memberRepository;
	private final MemberEntityConverter memberConverter;
	private final CommandMemberResponseConverter responseConverter;

	@Transactional
	@Override
	public CommandMemberResponse execute(
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

	private MemberEntity updateActiveStatus(final MemberModel model, final String status) {
		model.updateActiveStatus(status);
		MemberEntity updatedMember = memberConverter.toEntity(model);
		return memberRepository.save(updatedMember);
	}
}
