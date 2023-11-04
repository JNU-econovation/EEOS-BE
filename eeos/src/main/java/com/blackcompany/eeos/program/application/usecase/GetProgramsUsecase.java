package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.GetProgramsResponse;
import com.blackcompany.eeos.program.application.dto.PageResponse;

public interface GetProgramsUsecase {

	PageResponse<GetProgramsResponse> getProgram(String status, int size, int page);
}
