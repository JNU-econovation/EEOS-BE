package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.ChangeStatusRequest;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.attend.application.exception.NotFoundAttendException;
import com.blackcompany.eeos.attend.application.model.AttendModel;
import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.application.usecase.ChangeStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.application.service.MemberService;
import com.blackcompany.eeos.program.application.service.ProgramValidService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendService implements GetAttendantInfoUsecase, ChangeStatusUsecase {

	private final AttendRepository attendRepository;
	private final ProgramValidService programValidService;
	private final AttendInfoConverter infoConverter;
	private final AttendEntityConverter entityConverter;
	private final MemberService memberService;

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId) {
		programValidService.validate(programId);

		List<AttendEntity> attendEntities = attendRepository.findAllByProgramId(programId);
		return attendEntities.stream()
				.map(
						attendEntity ->
								infoConverter.from(
										memberService.findMemberInfo(attendEntity.getId()), attendEntity.getStatus()))
				.collect(Collectors.toList());
	}

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId, final String status) {
		programValidService.validate(programId);

		List<AttendEntity> attendEntities = attendRepository.findAllByProgramId(programId);

		return attendEntities.stream()
				.map(entityConverter::from)
				.filter(model -> model.isSame(status))
				.map(
						attendEntity ->
								infoConverter.from(
										memberService.findMemberInfo(attendEntity.getId()), attendEntity.getStatus()))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void changeStatus(final ChangeStatusRequest request, final Long programId) {
		AttendModel model =
				attendRepository
						.findByProgramIdAndMemberId(programId, request.getMemberId())
						.map(entityConverter::from)
						.orElseThrow(NotFoundAttendException::new);

		model.isSame(request.getBeforeAttendStatus());

		model.changeStatus(request.getAfterAttendStatus());
		attendRepository.save(entityConverter.toEntity(model));
	}
}
