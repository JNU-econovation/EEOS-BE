package com.blackcompany.eeos.program.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.blackcompany.eeos.program.application.model.ProgramStatus;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ActiveProgramStatusServiceTest {
	@InjectMocks ActiveProgramStatusService activeProgramStateService;

	@Mock ProgramRepository programRepository;

	@Test
	@DisplayName("해당 클래스의 status는 active이다.")
	void support() {
		// when
		ProgramStatus support = activeProgramStateService.support();

		// then
		assertEquals(ProgramStatus.ACTIVE, support);
	}

	@Test
	@DisplayName("진행중인 프로그램을 불러온다.")
	void getPages() {
		// given
		Timestamp now = Timestamp.from(Instant.now());
		int page = 0;
		int size = 1;
		PageRequest pageRequest = PageRequest.of(page, size);

		// when
		Page<ProgramEntity> pages =
				activeProgramStateService.getPages(ProgramCategory.WEEKLY, now, pageRequest);

		// then
		verify(programRepository).findAllByIng(ProgramCategory.WEEKLY, now, pageRequest);
	}
}
