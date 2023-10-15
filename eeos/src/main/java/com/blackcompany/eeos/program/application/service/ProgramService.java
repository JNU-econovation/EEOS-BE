package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.program.application.domain.ProgramModel;
import com.blackcompany.eeos.program.application.dto.AbstractProgramRequest;
import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.model.converter.ProgramRequestConverter;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService implements CreateProgramUsecase, GetProgramUsecase {
	private final ProgramRequestConverter requestConverter;
	private final ProgramEntityConverter entityConverter;
	private final ProgramResponseConverter responseConverter;
	private final ProgramRepository programRepository;

	@Override
	public Long create(AbstractProgramRequest request) {
		ProgramModel model = requestConverter.from(request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity save = programRepository.save(entity);

		return save.getId();
	}

	@Override
	public GetProgramResponse getProgram(Long id) {
		ProgramEntity programEntity =
				programRepository
						.findById(id)
						.orElseThrow(() -> new NotFoundProgramException("존재하지 않는 프로그램입니다"));

		ProgramModel model = entityConverter.from(programEntity);
		return responseConverter.from(model, model.calculateEventStatus());
	}
}
