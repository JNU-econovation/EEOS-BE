package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.attend.application.service.CandidateService;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.domain.ProgramModel;
import com.blackcompany.eeos.program.application.domain.ProgramStatus;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import com.blackcompany.eeos.program.application.dto.GetProgramsResponse;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.dto.converter.ProgramPageResponseConverter;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.model.converter.ProgramRequestConverter;
import com.blackcompany.eeos.program.application.support.ProgramStatusFactory;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramsUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService
		implements CreateProgramUsecase, GetProgramUsecase, UpdateProgramUsecase, GetProgramsUsecase {

	private final ProgramRequestConverter requestConverter;
	private final ProgramEntityConverter entityConverter;
	private final ProgramResponseConverter responseConverter;
	private final ProgramRepository programRepository;
	private final CandidateService candidateService;
	private final ProgramPageResponseConverter pageResponseConverter;
	private final ProgramStatusFactory programStatusFactory;

	@Override
	public CommandProgramResponse create(CreateProgramRequest request) {
		ProgramModel model = requestConverter.from(request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity save = programRepository.save(entity);

		candidateService.saveCandidate(save.getId());

		return responseConverter.from(save.getId());
	}

	@Override
	public GetProgramResponse getProgram(Long id) {
		ProgramEntity programEntity =
				programRepository.findById(id).orElseThrow(() -> new NotFoundProgramException(id));

		ProgramModel model = entityConverter.from(programEntity);
		return responseConverter.from(model, model.calculate());
	}

	@Override
	public CommandProgramResponse update(Long programId, UpdateProgramRequest request) {
		ProgramModel model = requestConverter.from(programId, request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity updateEntity = programRepository.save(entity);

		return responseConverter.from(updateEntity.getId());
	}

	@Override
	public PageResponse<GetProgramsResponse> getPrograms(String status, int size, int page) {
		Timestamp now = DateConverter.toEpochSecond(LocalDate.now());
		PageRequest pageRequest = PageRequest.of(page, size);

		Map<ProgramStatus, ProgramStateService> programStatusStrategy = programStatusFactory.make();

		ProgramStateService programStateService =
				programStatusStrategy.get(ProgramStatus.getStatus(status));
		Page<ProgramEntity> pages = programStateService.getPages(now, pageRequest);
		return pageResponseConverter.from(pages);
	}
}
