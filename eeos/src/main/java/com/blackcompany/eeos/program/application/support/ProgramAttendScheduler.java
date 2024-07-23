package com.blackcompany.eeos.program.application.support;

import com.blackcompany.eeos.program.application.model.ProgramAttendMode;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.program.persistence.RedisDelayedQueue;
import com.blackcompany.eeos.target.application.event.EndAttendModeEvent;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProgramAttendScheduler {

	private final RedisDelayedQueue redisDelayedQueue;
	private final ProgramRepository programRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void quitAttend() {
		Set<Long> jobs = redisDelayedQueue.getReadyTasks();
		jobs.forEach(
				id -> {
					programRepository.changeAttendMode(id, ProgramAttendMode.END);
					eventPublisher.publishEvent(EndAttendModeEvent.of(id));
				});
	}
}
