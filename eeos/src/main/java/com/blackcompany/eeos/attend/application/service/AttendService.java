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
	private final MemberService memberService;
	private final AttendInfoConverter infoConverter;
	private final AttendEntityConverter entityConverter;

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId) {
		List<AttendEntity> attendEntities = attendRepository.findAllByProgramId(programId);
		return attendEntities.stream()
				.map(
						attendEntity ->
								infoConverter.from(
										memberService.findMemberInfo(attendEntity.getId()),
										attendEntity.getStatus().getStatus()))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void changeStatus(final ChangeStatusRequest request, final Long programId) {
		AttendEntity entity =
				attendRepository
						.findByProgramIdAndMemberId(programId, request.getMemberId())
						.orElseThrow(NotFoundAttendException::new);

		AttendModel attendModel = entityConverter.from(entity);

		attendModel.validateBeforeStatus(request.getBeforeAttendStatus());
		attendModel.changeStatus(request.getAfterAttendStatus());

		attendRepository.save(entityConverter.toEntity(attendModel));
	}
}
