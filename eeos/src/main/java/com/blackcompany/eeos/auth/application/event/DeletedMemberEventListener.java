package com.blackcompany.eeos.auth.application.event;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.persistence.InvalidTokenRepository;
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
	private final AttendRepository attendRepository;
	private final InvalidTokenRepository invalidTokenRepository;
	private final TokenResolver tokenResolver;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleDeletedProgram(DeletedMemberEvent event) {
		deleteTargetData(event.getMemberId());
		saveUsedToken(event.getToken(), event.getMemberId());
	}

	private void deleteTargetData(Long memberId) {
		attendRepository.deleteAllByMemberId(memberId);
	}

	private void saveUsedToken(String token, Long memberId) {
		invalidTokenRepository.save(token, memberId, getExpiredDate(token));
	}

	private Long getExpiredDate(String token) {
		return tokenResolver.getExpiredDateByRefreshToken(token);
	}
}
