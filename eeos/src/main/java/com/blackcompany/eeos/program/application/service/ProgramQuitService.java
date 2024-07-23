package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.usecase.ProgramQuitUsecase;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.program.persistence.RedisDelayedQueue;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramQuitService implements ProgramQuitUsecase {

	private final RedisDelayedQueue redisDelayedQueue;
	private final ProgramRepository programRepository;

	@Override
	public void pushQuitAttendJob(ProgramModel model) {
		long delayedTime = model.getProgramDate().getTime() - Instant.now().toEpochMilli();
		redisDelayedQueue.addTask(model.getId(), delayedTime);
	}
}
