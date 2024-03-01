package com.blackcompany.eeos.teamBuilding.application.event;

import com.blackcompany.eeos.target.persistence.TeamBuildingTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletedTeamBuildingEventListener {
	private final TeamBuildingTargetRepository teamBuildingTargetRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleDeletedTeamBuilding(DeletedTeamBuildingEvent event) {
		log.info(
				"팀빌딩 삭제 Transaction committed: {}",
				TransactionSynchronizationManager.isActualTransactionActive());
		teamBuildingTargetRepository.deleteByTeamBuildingId(event.getTeamBuildingId());
	}
}
