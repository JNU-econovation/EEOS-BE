package com.blackcompany.eeos.program.application.service;

import static org.junit.jupiter.api.Assertions.*;
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
class EndProgramStatusServiceTest {

	@InjectMocks EndProgramStatusService endProgramStateService;

	@Mock ProgramRepository programRepository;

	@Test
	@DisplayName("해당 클래스의 status는 end이다.")
	void support() {
		// when
		ProgramStatus support = endProgramStateService.support();

		// then
		assertEquals(ProgramStatus.END, support);
	}

	@Test
	@DisplayName("완료된 프로그램을 불러온다.")
	void getPages() {
		// given
		Timestamp now = Timestamp.from(Instant.now());
		int page = 0;
		int size = 1;
		PageRequest pageRequest = PageRequest.of(page, size);

		// when
		Page<ProgramEntity> pages =
				endProgramStateService.getPages(ProgramCategory.WEEKLY, now, pageRequest);

		// then
		verify(programRepository).findAllByEnd(ProgramCategory.WEEKLY, now, pageRequest);
	}
}
