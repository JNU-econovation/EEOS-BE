package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.attend.application.service.CandidateService;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.ProgramsResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.dto.converter.ProgramPageResponseConverter;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.ProgramStatus;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.model.converter.ProgramRequestConverter;
import com.blackcompany.eeos.program.application.support.ProgramStatusServiceComposite;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramsUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramService
		implements CreateProgramUsecase, GetProgramUsecase, UpdateProgramUsecase, GetProgramsUsecase {

	private final ProgramRequestConverter requestConverter;
	private final ProgramEntityConverter entityConverter;
	private final ProgramResponseConverter responseConverter;
	private final ProgramRepository programRepository;
	private final CandidateService candidateService;
	private final ProgramPageResponseConverter pageResponseConverter;
	private final ProgramStatusServiceComposite programStatusComposite;

	@Override
	@Transactional
	public CommandProgramResponse create(final Long memberId, final CreateProgramRequest request) {
		ProgramModel model = requestConverter.from(memberId, request);
		ProgramEntity entity = entityConverter.toEntity(model);

		ProgramEntity save = programRepository.save(entity);
		candidateService.saveCandidate(save.getId(), request.getMembers());

		return responseConverter.from(save.getId());
	}

	@Override
	public QueryProgramResponse getProgram(Long id) {
		ProgramEntity programEntity =
				programRepository.findById(id).orElseThrow(() -> new NotFoundProgramException(id));

		ProgramModel model = entityConverter.from(programEntity);
		return responseConverter.from(model, model.calculate());
	}

	@Override
	@Transactional
	public CommandProgramResponse update(Long programId, UpdateProgramRequest request) {
		ProgramModel model = requestConverter.from(programId, request);
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity updateEntity = programRepository.save(entity);

		return responseConverter.from(updateEntity.getId());
	}

	@Override
	public PageResponse<QueryProgramsResponse> getPrograms(String status, int size, int page) {
		Timestamp now = DateConverter.toEpochSecond(LocalDate.now());
		PageRequest pageRequest = PageRequest.of(page, size);

		Page<ProgramEntity> pages =
				programStatusComposite.getPages(ProgramStatus.getStatus(status), now, pageRequest);

		ProgramsResponse response = entityConverter.from(pages);
		return pageResponseConverter.from(response);
	}
}
