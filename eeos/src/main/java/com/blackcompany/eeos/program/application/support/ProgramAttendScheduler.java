package com.blackcompany.eeos.program.application.support;

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
	private final ApplicationEventPublisher eventPublisher;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void quitAttend() {
		Set<Long> jobs = redisDelayedQueue.getReadyTasks();
		eventPublisher.publishEvent(EndAttendModeEvent.of(jobs));
	}
}
