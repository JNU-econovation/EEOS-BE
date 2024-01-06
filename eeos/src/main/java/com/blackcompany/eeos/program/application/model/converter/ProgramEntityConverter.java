package com.blackcompany.eeos.program.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.program.application.dto.ProgramsResponse;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ProgramEntityConverter
		implements AbstractEntityConverter<ProgramEntity, ProgramModel> {

	@Override
	public ProgramModel from(ProgramEntity source) {
		return ProgramModel.builder()
				.id(source.getId())
				.title(source.getTitle())
				.programDate(source.getProgramDate())
				.content(source.getContent())
				.programCategory(source.getProgramCategory())
				.programType(source.getProgramType())
				.writer(source.getWriter())
				.build();
	}

	public ProgramsResponse from(Page<ProgramEntity> pages) {
		return ProgramsResponse.builder()
				.page(pages)
				.programs(pages.getContent().stream().map(this::from).collect(Collectors.toList()))
				.build();
	}

	@Override
	public ProgramEntity toEntity(ProgramModel source) {
		return ProgramEntity.builder()
				.id(source.getId())
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(source.getProgramDate())
				.programCategory(source.getProgramCategory())
				.programType(source.getProgramType())
				.writer(source.getWriter())
				.build();
	}
}
