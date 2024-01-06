package com.blackcompany.eeos.program.application.support;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.model.ProgramStatus;
import com.blackcompany.eeos.program.application.service.ProgramStatusService;
import com.blackcompany.eeos.program.fixture.ProgramStatusServiceFixture;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class ProgramStatusServiceCompositeTest {

	@Test
	@DisplayName("원하는 프로그램 상태를 전달하면 해당 상태의 프로그램을 전달받는다.")
	void getPages() {
		// given
		Set<ProgramStatusService> set = new HashSet<>();
		set.add(new ProgramStatusServiceFixture());
		ProgramStatusServiceComposite composite = new ProgramStatusServiceComposite(set);

		Timestamp now = DateConverter.toEpochSecond(LocalDate.now());

		// when
		Page<ProgramEntity> result =
				composite.getPages(ProgramCategory.ETC, ProgramStatus.ACTIVE, now, PageRequest.of(0, 1));

		// then
		assertEquals(result.getContent().size(), 1);
	}
}
