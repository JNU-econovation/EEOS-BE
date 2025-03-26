package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.common.presentation.response.PageResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;

public interface GetProgramsUsecase {

	PageResponse<QueryProgramsResponse> getPrograms(
			String category, String status, int size, int page);
}
