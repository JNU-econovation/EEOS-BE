package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.model.ProgramAttendMode;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.support.DelayedQueue;
import com.blackcompany.eeos.program.application.usecase.ProgramQuitUsecase;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramQuitService implements ProgramQuitUsecase {

	private final DelayedQueue delayedQueue;
	private final AttendRepository attendRepository;
	private final ProgramRepository programRepository;
	private final String KEY = "quit_program_reservation";

	@Override
	public void reserveQuitProgram(ProgramModel model) {
		// programDate 를 score 로 사용
		long programDate = model.getProgramDate().getTime() / 1000;

		delayedQueue.addTask(KEY, model.getId(), programDate);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void quitAttend() {
		log.info("출석 체크 자동 종료 시작");
		try {
			long programDate = DateConverter.toEpochSecond(LocalDate.now()).getTime();

			Set<Long> jobs = getReadyTasks(programDate);

			Set<Long> completedIds = doQuit(jobs);

			removeCompleteTask(completedIds);

		} catch (Exception e) {
			log.error("행사 자동 종료 중 에러가 발생하였습니다. {}", e.getMessage());
			throw e;
		}
	}

	private Set<Long> doQuit(Set<Long> programIds) {
		Set<Long> completedIds = new HashSet<>();
		for (Long id : programIds) {
			log.info("출석 체크 자동 종료 (programId : {})", id);

			programRepository.changeAttendMode(id, ProgramAttendMode.END);
			attendRepository.updateAttendStatusByProgramId(
					id, AttendStatus.NONRESPONSE, AttendStatus.ABSENT);

			completedIds.add(id);
		}
		return completedIds;
	}

	private Set<Long> getReadyTasks(long programDate) {
		Set<Long> jobs =
				delayedQueue.getReadyTasks(KEY, (double) programDate).stream()
						.map(id -> Long.parseLong(id.toString()))
						.collect(Collectors.toSet());
		return jobs;
	}

	private void removeCompleteTask(Set<Long> programIds) {
		delayedQueue.removeByValue(KEY, programIds);
	}
}
