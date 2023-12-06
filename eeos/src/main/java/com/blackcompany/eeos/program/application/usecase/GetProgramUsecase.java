package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import org.springframework.stereotype.Component;

@Component
public interface GetProgramUsecase {
	QueryProgramResponse getProgram(Long id);
}
