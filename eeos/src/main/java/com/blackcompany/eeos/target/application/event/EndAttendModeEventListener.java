package com.blackcompany.eeos.target.application.event;

import com.blackcompany.eeos.program.application.model.ProgramAttendMode;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.persistence.AttendRepository;
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
public class EndAttendModeEventListener {

	private final AttendRepository attendRepository;
	private final ProgramRepository programRepository;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleDeletedProgram(EndAttendModeEvent event) {
		log.info(
				"출석 체크 종료 Transaction committed: {}",
				TransactionSynchronizationManager.isActualTransactionActive());

		for (Long id : event.getProgramIds()) {
			programRepository.changeAttendMode(id, ProgramAttendMode.END);
			attendRepository.updateAttendStatusByProgramId(
					id, AttendStatus.NONRESPONSE, AttendStatus.ABSENT);
		}

		if (event.getProgramIds().isEmpty()) {
			log.info("Empty Set");
		}
	}
}
