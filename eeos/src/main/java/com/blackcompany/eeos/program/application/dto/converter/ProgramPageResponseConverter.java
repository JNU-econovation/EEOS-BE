package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.ProgramsResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.ProgramStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramPageResponseConverter {
	private final ProgramResponseConverter programResponseConverter;

	public PageResponse<QueryProgramsResponse> from(
			ProgramsResponse source, ProgramStatus programStatus) {
		Page page = source.getPage();
		List<ProgramModel> programs = source.getPrograms();

		return PageResponse.<QueryProgramsResponse>builder()
				.size(page.getSize())
				.page(page.getNumber())
				.totalPage(page.getTotalPages())
				.programs(
						programs.stream()
								.map(program -> programResponseConverter.from(program, programStatus))
								.collect(Collectors.toList()))
				.build();
	}
}
