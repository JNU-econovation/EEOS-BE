package com.blackcompany.eeos.program.application.event;

import com.blackcompany.eeos.attend.application.service.CommandAttendService;
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
public class DeletedProgramEventListener {
	private final CommandAttendService attendService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleDeletedProgram(DeletedProgramEvent event) {
		log.info(
				"Transaction committed: {}", TransactionSynchronizationManager.isActualTransactionActive());
		attendService.deleteByProgram(event.getProgramId());
	}
}
