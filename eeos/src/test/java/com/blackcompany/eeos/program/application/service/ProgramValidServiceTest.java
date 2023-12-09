package com.blackcompany.eeos.program.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProgramValidServiceTest {
	@InjectMocks ProgramValidService programValidService;

	@Mock ProgramRepository programRepository;

	@Test
	@DisplayName("프로그램이 존재하지 않으면 예외가 발생한다.")
	void validateException() {
		// given
		Mockito.when(programRepository.existsById(Mockito.anyLong())).thenReturn(Boolean.FALSE);

		// when & then
		assertThrows(NotFoundProgramException.class, () -> programValidService.validate(1L));
	}

	@Test
	@DisplayName("프로그램이 존재하면 예외가 발생하지 않는다.")
	void validateSuccess() {
		// given
		Mockito.when(programRepository.existsById(Mockito.anyLong())).thenReturn(Boolean.TRUE);

		// when & then
		assertDoesNotThrow(() -> programValidService.validate(1L));
	}
}
