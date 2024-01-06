package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.persistence.AttendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommandAttendService {
	private final AttendRepository attendRepository;

	@Transactional
	public void deleteByProgram(Long programId) {
		attendRepository.deleteAllByProgramId(programId);
	}
}
