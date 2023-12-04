package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramValidService {

	private final ProgramRepository programRepository;

	public void validate(Long programId) {
		if (!programRepository.existsById(programId)) {
			throw new NotFoundProgramException(programId);
		}
	}
}
