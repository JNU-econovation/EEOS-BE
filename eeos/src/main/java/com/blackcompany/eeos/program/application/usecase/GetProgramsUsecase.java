package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.dto.PageResponse;

public interface GetProgramsUsecase {

	PageResponse<QueryProgramsResponse> getPrograms(String status, int size, int page);
}
