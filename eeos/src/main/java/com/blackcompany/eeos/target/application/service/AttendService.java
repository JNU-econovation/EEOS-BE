package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.target.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoActiveStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.target.application.dto.converter.ChangeAttendStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendActiveStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendStatusResponseConverter;
import com.blackcompany.eeos.target.application.exception.NotFoundAttendException;
import com.blackcompany.eeos.target.application.model.AttendModel;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.target.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendAllInfoSortActiveStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.target.persistence.AttendEntity;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendService
		implements GetAttendantInfoUsecase,
				ChangeAttendStatusUsecase,
				GetAttendStatusUsecase,
				GetAttendAllInfoSortActiveStatusUsecase {

	private final AttendRepository attendRepository;
	private final MemberRepository memberRepository;
	private final AttendInfoConverter infoConverter;
	private final AttendEntityConverter attendEntityConverter;
	private final QueryMemberService queryMemberService;
	private final ChangeAttendStatusConverter changeAttendStatusConverter;
	private final MemberEntityConverter memberEntityConverter;
	private final AttendInfoConverter attendInfoConverter;
	private final QueryAttendStatusResponseConverter attendStatusResponseConverter;
	private final AttendInfoActiveStatusConverter attendInfoActiveStatusConverter;
	private final QueryAttendActiveStatusConverter queryAttendActiveStatusConverter;
	private final ProgramRepository programRepository;

	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId) {
		validateExistsProgram(programId);

		return memberRepository.findMembersByProgramId(programId).stream()
				.map(member -> infoConverter.from(member, getAttendStatus(member.getId(), programId)))
				.collect(Collectors.toList());
	}

	@Override
	public QueryAttendStatusResponse findAttendInfo(final Long programId, final String attendStatus) {
		validateExistsProgram(programId);

		List<AttendModel> attends = findAttendByAttendStatus(programId, attendStatus);
		List<MemberModel> members = findMembers(attends);

		List<AttendInfoResponse> response =
				members.stream()
						.map(member -> combine(member, attends, programId))
						.collect(Collectors.toList());

		return attendStatusResponseConverter.of(response);
	}

	@Transactional
	@Override
	public ChangeAttendStatusResponse changeStatus(
			final Long memberId, final ChangeAttendStatusRequest request, final Long programId) {
		AttendModel model = getAttend(memberId, programId);

		AttendModel changedModel =
				model.changeStatus(request.getBeforeAttendStatus(), request.getAfterAttendStatus());
		AttendEntity updated = attendRepository.save(attendEntityConverter.toEntity(changedModel));

		String name = queryMemberService.getName(memberId);
		return changeAttendStatusConverter.from(name, updated.getStatus().getStatus());
	}

	@Override
	public ChangeAttendStatusResponse getStatus(Long memberId, Long programId) {
		boolean isAttend = isExistAttend(memberId, programId);
		String name = queryMemberService.getName(memberId);

		if (isAttend) {
			AttendModel existModel = getAttend(memberId, programId);
			return changeAttendStatusConverter.from(name, existModel.getStatus());
		}

		return changeAttendStatusConverter.from(name, AttendStatus.NONRELATED.getStatus());
	}

	@Override
	public QueryAttendActiveStatusResponse getAttendInfo(Long programId, String activeStatus) {
		List<MemberModel> members = findMembersByActiveStatus(activeStatus);

		List<AttendInfoActiveStatusResponse> response =
				members.stream()
						.map(member -> combine(member, findAttend(programId), member.getActiveStatus()))
						.collect(Collectors.toList());

		return queryAttendActiveStatusConverter.of(response);
	}

	private AttendModel getAttend(final Long memberId, final Long programId) {
		return attendRepository
				.findByProgramIdAndMemberId(programId, memberId)
				.map(attendEntityConverter::from)
				.orElseThrow(() -> new NotFoundAttendException(programId));
	}

	private boolean isExistAttend(final Long memberId, final Long programId) {
		return attendRepository.findByProgramIdAndMemberId(programId, memberId).isPresent();
	}

	private String getAttendStatus(final Long memberId, final Long programId) {
		return getAttend(memberId, programId).getStatus();
	}

	private AttendInfoResponse combine(
			MemberModel member, List<AttendModel> attends, Long programId) {

		AttendModel model =
				attends.stream()
						.filter(attend -> member.validateSame(attend.getMemberId()))
						.findAny()
						.orElseThrow(() -> new NotFoundAttendException(programId));

		return attendInfoConverter.from(member, model.getStatus());
	}

	private AttendInfoActiveStatusResponse combine(
			MemberModel member, List<AttendModel> attends, String activeStatus) {

		AttendModel model =
				attends.stream()
						.filter(attend -> member.validateSame(attend.getMemberId()))
						.findAny()
						.orElse(AttendModel.of());

		return attendInfoActiveStatusConverter.from(member, model.getStatus(), activeStatus);
	}

	private List<MemberModel> findMembers(List<AttendModel> attends) {
		List<Long> memberIds =
				attends.stream().map(AttendModel::getMemberId).collect(Collectors.toList());

		return memberRepository.findMembersByIds(memberIds).stream()
				.map(memberEntityConverter::from)
				.collect(Collectors.toList());
	}

	private List<AttendModel> findAttendByAttendStatus(final Long programId, final String status) {
		AttendStatus attendStatus = AttendStatus.find(status);
		return attendRepository.findAllByProgramIdAndStatus(programId, attendStatus).stream()
				.map(attendEntityConverter::from)
				.collect(Collectors.toList());
	}

	private List<AttendModel> findAttend(final Long programId) {
		return attendRepository.findAllByProgramId(programId).stream()
				.map(attendEntityConverter::from)
				.collect(Collectors.toList());
	}

	private List<MemberModel> findMembersByActiveStatus(final String activeStatus) {
		if (ActiveStatus.isSame(activeStatus, ActiveStatus.ALL)) {
			return memberRepository.findMembers().stream()
					.map(memberEntityConverter::from)
					.collect(Collectors.toList());
		}

		return memberRepository.findMembersByActiveStatus(ActiveStatus.find(activeStatus)).stream()
				.map(memberEntityConverter::from)
				.collect(Collectors.toList());
	}

	private void validateExistsProgram(Long programId) {
		if (!programRepository.existsById(programId)) {
			throw new NotFoundProgramException(programId);
		}
	}
}
