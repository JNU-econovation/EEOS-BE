package com.blackcompany.eeos.program.application.support;

import com.blackcompany.eeos.program.application.domain.ProgramStatus;
import com.blackcompany.eeos.program.application.service.ProgramStateService;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ProgramStatusServiceComposite {
	private final Map<ProgramStatus, ProgramStateService> programs;

	public ProgramStatusServiceComposite(Set<ProgramStateService> programStateServices) {
		this.programs =
				programStateServices.stream()
						.collect(Collectors.toMap(ProgramStateService::support, Function.identity()));
	}

	public Page<ProgramEntity> getPages(
			ProgramStatus programStatus, Timestamp now, PageRequest pageRequest) {
		return getProgramStatusService(programStatus).getPages(now, pageRequest);
	}

	private ProgramStateService getProgramStatusService(ProgramStatus programStatus) {
		return programs.get(programStatus);
	}
}
