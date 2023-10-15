package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import org.springframework.stereotype.Component;

@Component
public interface GetProgramUsecase {
	GetProgramResponse getProgram(Long id);
}
