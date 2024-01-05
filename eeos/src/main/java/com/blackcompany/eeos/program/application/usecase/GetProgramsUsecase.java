package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;

public interface GetProgramsUsecase {

	PageResponse<QueryProgramResponse> getPrograms(
			String category, String status, int size, int page);
}
