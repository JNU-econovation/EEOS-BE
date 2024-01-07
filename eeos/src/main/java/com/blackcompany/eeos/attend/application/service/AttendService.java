package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.attend.application.dto.converter.ChangeAttendStatusConverter;
import com.blackcompany.eeos.attend.application.dto.converter.QueryAttendStatusResponseConverter;
import com.blackcompany.eeos.attend.application.exception.NotFoundAttendException;
import com.blackcompany.eeos.attend.application.model.AttendModel;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.member.persistence.MemberEntity;
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
	private final MemberEntityConverter memberEntityConverter;
	private final AttendInfoConverter attendInfoConverter;
	private final QueryAttendStatusResponseConverter attendStatusResponseConverter;

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId) {
		programValidService.validate(programId);

		return memberRepository.findMembersByProgramId(programId).stream()
				.map(member -> infoConverter.from(member, getAttendStatus(member.getId(), programId)))
				.collect(Collectors.toList());
	}

	@Override
	public QueryAttendStatusResponse findAttendInfo(final Long programId, final String status) {
		programValidService.validate(programId);

		List<AttendEntity> attendsByProgram = findAttend(programId, status);
		List<MemberModel> members =
				findAMembers(programId, status).stream()
						.map(memberEntityConverter::from)
						.collect(Collectors.toList());

		List<AttendInfoResponse> response =
				members.stream()
						.map(member -> combine(member, attendsByProgram, programId))
						.collect(Collectors.toList());

		return attendStatusResponseConverter.of(response);
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

	@Override
	public ChangeAttendStatusResponse getStatus(Long memberId, Long programId) {
		boolean isAttend = getAttendDefault(memberId, programId);
		String name = queryMemberService.getName(memberId);

		if (isAttend) {
			AttendModel model = attendEntityConverter.from(getAttend(memberId, programId));
			return changeAttendStatusConverter.from(name, model.getStatus().getStatus());
		}

		return changeAttendStatusConverter.from(name, AttendStatus.NONRELATED.getStatus());
	}

	private AttendEntity getAttend(final Long memberId, final Long programId) {
		return attendRepository
				.findByProgramIdAndMemberId(programId, memberId)
				.orElseThrow(() -> new NotFoundAttendException(programId));
	}

	private boolean getAttendDefault(final Long memberId, final Long programId) {
		return attendRepository.findByProgramIdAndMemberId(programId, memberId).isPresent();
	}

	private AttendStatus getAttendStatus(final Long memberId, final Long programId) {
		return getAttend(memberId, programId).getStatus();
	}

	private AttendInfoResponse combine(
			MemberModel member, List<AttendEntity> attends, Long programId) {
		AttendEntity attendEntity =
				attends.stream()
						.filter(attend -> member.validateSame(attend.getMemberId()))
						.findAny()
						.orElseThrow(() -> new NotFoundAttendException(programId));

		return attendInfoConverter.from(member, attendEntity.getStatus());
	}

	private List<MemberEntity> findAMembers(final Long programId, final String status) {
		List<Long> memberIds =
				findAttend(programId, status).stream()
						.map(AttendEntity::getMemberId)
						.collect(Collectors.toList());

		return memberRepository.findUsersByIds(memberIds);
	}

	private List<AttendEntity> findAttend(final Long programId, final String status) {
		AttendStatus attendStatus = AttendStatus.findByAttendStatus(status);

		return attendRepository.findAllByProgramIdAndStatus(programId, attendStatus);
	}
}
