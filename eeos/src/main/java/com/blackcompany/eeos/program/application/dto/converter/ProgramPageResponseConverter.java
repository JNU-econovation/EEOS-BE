package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.dto.GetProgramsResponse;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramPageResponseConverter {
	private final ProgramResponseConverter responseConverter;

	public PageResponse<GetProgramsResponse> from(Page<ProgramEntity> page) {
		Pageable pageable = page.getPageable();
		List<ProgramEntity> source = page.getContent();

		return PageResponse.<GetProgramsResponse>builder()
				.size(pageable.getPageSize())
				.page(pageable.getPageNumber())
				.totalPage(page.getTotalPages())
				.programs(source.stream().map(responseConverter::from).collect(Collectors.toList()))
				.build();
	}
}
