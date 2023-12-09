package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;

public interface GetProgramUsecase {
	QueryProgramResponse getProgram(Long id);
}
