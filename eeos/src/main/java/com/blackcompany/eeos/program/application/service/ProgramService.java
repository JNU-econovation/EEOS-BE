package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.attend.application.service.CandidateService;
import com.blackcompany.eeos.program.application.domain.ProgramModel;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.dto.suppport.AbstractProgramRequest;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.model.converter.ProgramRequestConverter;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService
		implements CreateProgramUsecase, GetProgramUsecase, UpdateProgramUsecase {
	private final ProgramRequestConverter requestConverter;
	private final ProgramEntityConverter entityConverter;
	private final ProgramResponseConverter responseConverter;
	private final ProgramRepository programRepository;
	private final CandidateService candidateService;

	@Override
	public CommandProgramResponse create(AbstractProgramRequest request) {
		ProgramModel model = requestConverter.from(request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity save = programRepository.save(entity);

		candidateService.saveCandidate(save.getId());

		return responseConverter.from(save.getId());
	}

	@Override
	public GetProgramResponse getProgram(Long id) {
		ProgramEntity programEntity =
				programRepository.findById(id).orElseThrow(NotFoundProgramException::new);

		ProgramModel model = entityConverter.from(programEntity);
		return responseConverter.from(model, model.calculateEventStatus());
	}

	@Override
	public CommandProgramResponse update(Long programId, AbstractProgramRequest request) {
		ProgramModel model = requestConverter.from(programId, request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity updateEntity = programRepository.save(entity);

		return responseConverter.from(updateEntity.getId());
	}
}
