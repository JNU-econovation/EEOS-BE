package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.attend.application.service.CandidateService;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.dto.ChangeAllAttendStatusRequest;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.ProgramsResponse;
import com.blackcompany.eeos.program.application.dto.QueryAccessRightResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.dto.converter.ProgramPageResponseConverter;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.dto.converter.QueryAccessRightResponseConverter;
import com.blackcompany.eeos.program.application.event.DeletedProgramEvent;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.ProgramStatus;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.model.converter.ProgramRequestConverter;
import com.blackcompany.eeos.program.application.support.ProgramStatusServiceComposite;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.DeleteProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetAccessRightUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramsUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramService
		implements CreateProgramUsecase,
				GetProgramUsecase,
				UpdateProgramUsecase,
				GetProgramsUsecase,
				DeleteProgramUsecase,
				GetAccessRightUsecase {

	private final ProgramRequestConverter requestConverter;
	private final ProgramEntityConverter entityConverter;
	private final ProgramResponseConverter responseConverter;
	private final ProgramRepository programRepository;
	private final CandidateService candidateService;
	private final ProgramPageResponseConverter pageResponseConverter;
	private final ProgramStatusServiceComposite programStatusComposite;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final QueryAccessRightResponseConverter accessRightResponseConverter;

	@Override
	@Transactional
	public CommandProgramResponse create(final Long memberId, final CreateProgramRequest request) {
		ProgramModel model = requestConverter.from(memberId, request);

		model.validateCreate();
		Long saveId = createProgram(model);

		candidateService.saveCandidate(saveId, request.getMembers());

		return responseConverter.from(saveId);
	}

	@Override
	public QueryProgramResponse getProgram(final Long memberId, final Long programId) {
		ProgramModel model = findProgram(programId);
		return responseConverter.from(
				model, model.getProgramStatus(), findAccessRight(model, memberId));
	}

	@Override
	@Transactional
	public CommandProgramResponse update(
			final Long writerId, final Long programId, final UpdateProgramRequest request) {
		ProgramModel model = findProgram(programId);
		ProgramModel requestModel = requestConverter.from(writerId, request, programId);

		updateProgram(model, requestModel);
		updateAttend(request.getMembers(), model);

		return responseConverter.from(model.getId());
	}

	@Override
	public PageResponse<QueryProgramsResponse> getPrograms(
			final String category, final String status, final int size, final int page) {
		Timestamp now = DateConverter.toEpochSecond(LocalDate.now());
		PageRequest pageRequest = PageRequest.of(page, size);
		ProgramCategory programCategory = ProgramCategory.find(category);
		ProgramStatus programStatus = ProgramStatus.getStatus(status);

		Page<ProgramEntity> pages =
				programStatusComposite.getPages(programCategory, programStatus, now, pageRequest);

		ProgramsResponse response = entityConverter.from(pages);
		return pageResponseConverter.from(response, programStatus);
	}

	@Override
	@Transactional
	public void delete(final Long memberId, final Long programId) {
		ProgramModel program = findProgram(programId);
		program.validateDelete(memberId);

		programRepository.deleteById(program.getId());
		applicationEventPublisher.publishEvent(DeletedProgramEvent.of(programId));
	}

	@Override
	public QueryAccessRightResponse getAccessRight(final Long memberId, final Long programId) {
		ProgramModel model = findProgram(programId);
		String accessRight = findAccessRight(model, memberId);

		return accessRightResponseConverter.to(accessRight);
	}

	private ProgramModel findProgram(final Long programId) {
		return programRepository
				.findById(programId)
				.map(entityConverter::from)
				.orElseThrow(() -> new NotFoundProgramException(programId));
	}

	private Long createProgram(ProgramModel model) {
		ProgramEntity entity = entityConverter.toEntity(model);
		ProgramEntity save = programRepository.save(entity);

		return save.getId();
	}

	private void updateProgram(ProgramModel model, ProgramModel requestModel) {
		ProgramModel update = model.update(requestModel);
		ProgramEntity entity = entityConverter.toEntity(update);
		programRepository.save(entity);
	}

	private void updateAttend(
			final List<ChangeAllAttendStatusRequest> members, final ProgramModel model) {

		if (members.isEmpty()) {
			return;
		}

		candidateService.updateCandidate(model.getId(), members);
	}

	private String findAccessRight(final ProgramModel model, final Long memberId) {
		return model.getAccessRight(memberId);
	}
}
