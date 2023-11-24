package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.attend.application.exception.NotFoundStatusException;
import com.blackcompany.eeos.attend.application.service.CandidateService;
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
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramsUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
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
				programRepository.findById(id).orElseThrow(NotFoundProgramException::new);

		ProgramModel model = entityConverter.from(programEntity);
		return responseConverter.from(model, model.calculateEventStatus());
	}

	@Override
	public CommandProgramResponse update(Long programId, UpdateProgramRequest request) {
		ProgramModel model = requestConverter.from(programId, request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity updateEntity = programRepository.save(entity);

		return responseConverter.from(updateEntity.getId());
	}

	@Override
	public PageResponse<GetProgramsResponse> getProgram(String status, int size, int page) {
		LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
		Timestamp now = Timestamp.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		PageRequest pageRequest = PageRequest.of(page, size);

		if (ProgramStatus.isSameStatus(status, ProgramStatus.ACTIVE)) {
			Page<ProgramEntity> pages = programRepository.findAllByIng(now, pageRequest);
			return pageResponseConverter.from(pages);
		}
		if (ProgramStatus.isSameStatus(status, ProgramStatus.END)) {
			Page<ProgramEntity> pages = programRepository.findAllByEnd(now, pageRequest);
			return pageResponseConverter.from(pages);
		}

		throw new NotFoundStatusException();
	}
}
