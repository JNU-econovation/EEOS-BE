package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.program.application.model.ProgramStatus;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import java.sql.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProgramStatusService {
	ProgramStatus support();

	Page<ProgramEntity> getPages(Timestamp now, PageRequest pageRequest);
}
