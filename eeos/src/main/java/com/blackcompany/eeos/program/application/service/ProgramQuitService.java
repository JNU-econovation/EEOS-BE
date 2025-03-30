package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.usecase.ProgramQuitUsecase;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.program.application.support.DelayedQueue;
import com.blackcompany.eeos.target.application.event.EndAttendModeEvent;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramQuitService implements ProgramQuitUsecase {

	private final DelayedQueue delayedQueue;
	private final ApplicationEventPublisher eventPublisher;
	private final String KEY = "quit_program_reservation";

	@Override
	public void pushQuitAttendJob(ProgramModel model) {
		// programDate 를 score 로 사용
		long programDate = model.getProgramDate().getTime() / 1000;

		delayedQueue.addTask(KEY, model.getId(), programDate);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void quitAttend() {
		try {
			Set<Long> jobs = getReadyTasks();
			eventPublisher.publishEvent(EndAttendModeEvent.of(jobs));
		} catch (Exception e) {
			log.error("[ProgramQuitService] 행사 자동 종료 중 에러가 발생하였습니다. {}", e.getMessage());
			throw e;
		}
	}

	private Set<Long> getReadyTasks(){
		long programDate = DateConverter.toEpochSecond(LocalDate.now()).getTime();

		Set<Long> jobs = delayedQueue.getReadyTasks(KEY, (double) programDate)
				.stream()
				.map(id -> (Long) id)
				.collect(Collectors.toSet());
		return jobs;
	}
}
