package com.blackcompany.eeos.program.fixture;

import com.blackcompany.eeos.program.application.model.ProgramStatus;
import com.blackcompany.eeos.program.application.service.ProgramStatusService;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import java.sql.Timestamp;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class ProgramStatusServiceFixture implements ProgramStatusService {
	@Override
	public ProgramStatus support() {
		return ProgramStatus.ACTIVE;
	}

	@Override
	public Page<ProgramEntity> getPages(ProgramCategory programCategory, Timestamp now, PageRequest pageRequest) {
		ProgramEntity program =
				ProgramEntity.builder().programCategory(ProgramCategory.WEEKLY).programDate(now).build();

		return new PageImpl<>(Collections.singletonList(program));
	}
}
