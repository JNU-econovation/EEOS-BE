package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.attend.application.dto.converter.ChangeAttendStatusConverter;
import com.blackcompany.eeos.attend.application.exception.NotFoundAttendException;
import com.blackcompany.eeos.attend.application.model.AttendModel;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.service.ProgramValidService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendService
		implements GetAttendantInfoUsecase, ChangeAttendStatusUsecase, GetAttendStatusUsecase {

	private final AttendRepository attendRepository;

	private final MemberRepository memberRepository;
	private final ProgramValidService programValidService;
	private final AttendInfoConverter infoConverter;
	private final AttendEntityConverter attendEntityConverter;
	private final QueryMemberService queryMemberService;
	private final ChangeAttendStatusConverter changeAttendStatusConverter;

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId) {
		programValidService.validate(programId);

		return memberRepository.findMembersByProgramId(programId).stream()
				.map(member -> infoConverter.from(member, getAttendStatus(member.getId(), programId)))
				.collect(Collectors.toList());
	}

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId, final String status) {
		programValidService.validate(programId);

		AttendStatus attendStatus = AttendStatus.findByAttendStatus(status);

		List<Long> statusMember =
				attendRepository.findAllByProgramIdAndStatus(programId, attendStatus).stream()
						.map(AttendEntity::getMemberId)
						.collect(Collectors.toList());

		return memberRepository.findMembersByProgramId(programId).stream()
				.filter(member -> statusMember.contains(member.getId()))
				.map(member -> infoConverter.from(member, status))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public ChangeAttendStatusResponse changeStatus(
			final Long memberId, final ChangeAttendStatusRequest request, final Long programId) {
		AttendModel model = attendEntityConverter.from(getAttend(memberId, programId));

		model.changeStatus(request.getBeforeAttendStatus(), request.getAfterAttendStatus());
		AttendEntity updated = attendRepository.save(attendEntityConverter.toEntity(model));

		String name = queryMemberService.getName(memberId);
		return changeAttendStatusConverter.from(name, updated.getStatus().getStatus());
	}

	private AttendEntity getAttend(final Long memberId, final Long programId) {
		return attendRepository
				.findByProgramIdAndMemberId(programId, memberId)
				.orElseThrow(() -> new NotFoundAttendException(programId));
	}

	private AttendStatus getAttendStatus(final Long memberId, final Long programId) {
		return getAttend(memberId, programId).getStatus();
	}

	@Override
	public ChangeAttendStatusResponse getStatus(Long memberId, Long programId) {
		AttendModel model = attendEntityConverter.from(getAttend(memberId, programId));
		String name = queryMemberService.getName(memberId);

		return changeAttendStatusConverter.from(name, model.getStatus().getStatus());
	}
}
