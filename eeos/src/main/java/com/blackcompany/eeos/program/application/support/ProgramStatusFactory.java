package com.blackcompany.eeos.program.application.support;

import com.blackcompany.eeos.program.application.domain.ProgramStatus;
import com.blackcompany.eeos.program.application.service.ActiveProgramStateService;
import com.blackcompany.eeos.program.application.service.EndProgramStateService;
import com.blackcompany.eeos.program.application.service.ProgramStateService;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramStatusFactory {
	private final ProgramRepository programRepository;

	public Map<ProgramStatus, ProgramStateService> make() {
		Map<ProgramStatus, ProgramStateService> stateServices = new EnumMap<>(ProgramStatus.class);

		stateServices.put(ProgramStatus.ACTIVE, new ActiveProgramStateService(programRepository));
		stateServices.put(ProgramStatus.END, new EndProgramStateService(programRepository));

		return stateServices;
	}
}
