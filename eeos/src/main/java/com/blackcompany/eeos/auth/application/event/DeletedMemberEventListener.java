package com.blackcompany.eeos.auth.application.event;

import com.blackcompany.eeos.auth.persistence.OAuthMemberRepository;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletedMemberEventListener {
	private final MemberRepository memberRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final AttendRepository attendRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleDeletedProgram(DeletedMemberEvent event) {
		deleteMemberData(event.getMemberId());
		deleteTargetData(event.getMemberId());
	}

	private void deleteMemberData(Long memberId) {
		memberRepository.deleteById(memberId);
		oAuthMemberRepository.findByMemberId(memberId).ifPresent(oAuthMemberRepository::delete);
	}

	private void deleteTargetData(Long memberId) {
		attendRepository.deleteAllByMemberId(memberId);
	}
}
