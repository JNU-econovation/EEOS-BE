package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.persistence.AttendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommandAttendService {
	private final AttendRepository attendRepository;

	@Transactional
	public void deleteByProgram(Long programId) {
		log.info("프로그램 삭제로 인한 해당 프로그램의 참석 정보 삭제");
		attendRepository.deleteAllByProgramId(programId);
	}
}
