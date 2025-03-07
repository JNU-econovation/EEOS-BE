package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.ProgramAttendMode;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.service.ProgramDateRangeService;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.target.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoWithProgramResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfosSearchRequest;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyResponse;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoActiveStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoWithProgramConverter;
import com.blackcompany.eeos.target.application.dto.converter.ChangeAttendStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendActiveStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendStatusResponseConverter;
import com.blackcompany.eeos.target.application.exception.DeniedChangeAttendException;
import com.blackcompany.eeos.target.application.exception.DeniedSaveAttendException;
import com.blackcompany.eeos.target.application.exception.NotFoundAttendException;
import com.blackcompany.eeos.target.application.exception.NotStartAttendException;
import com.blackcompany.eeos.target.application.model.AttendModel;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.target.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendAllInfoSortActiveStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.target.persistence.AttendEntity;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
	private final AttendInfoWithProgramConverter attendInfoWithProgramConverter;
	private final ProgramDateRangeService programDateRangeService;
	private final ProgramRepository programRepository;
	private final ProgramEntityConverter programEntityConverter;

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
	public ChangeAttendStatusResponse changeStatus(final Long memberId, final Long programId) {
		AttendModel model = getAttend(memberId, programId);

		ProgramModel program = findProgram(programId);

		validateAttend(program, model);

		AttendModel changedModel = model.changeStatus(program.getAttendMode().getMode());

		if (changedModel.getStatus().equals("attend")) {
			Long rank = calculateRank(programId);
			changedModel.setRank(rank);
		}

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
						.filter(m -> !m.isAdmin())
						.map(member -> combine(member, findAttend(programId), member.getActiveStatus()))
						.collect(Collectors.toList());

		return queryAttendActiveStatusConverter.of(response);
	}

	@Override
	public QueryAttendStatusResponse findFireFingerMembers(final Long programId) {
		validateExistsProgram(programId);

		List<AttendModel> attendModels = findTop5Attendants(programId);
		List<MemberModel> members = findMembers(attendModels);

		List<AttendInfoResponse> response =
				members.stream()
						.map(member -> combine(member, attendModels, programId))
						.collect(Collectors.toList());

		return attendStatusResponseConverter.of(response);
	}

	private List<AttendModel> findTop5Attendants(Long programId) {
		return attendRepository
				.findTop5ByProgramIdAndStatusOrderByUpdatedDateAscRankAsc(programId, AttendStatus.ATTEND)
				.stream()
				.map(attendEntityConverter::from)
				.collect(Collectors.toList());
	}

	public List<AttendInfoWithProgramResponse> findMyAttendInfo(
			Long memberId, AttendInfosSearchRequest request) {

		Long startDate = request.getStartDate();
		Long endDate = request.getEndDate();
		int size = request.getSize();
		int page = request.getPage();

		// 필요한 정보 : ProgramModel , AttendModel, MemberId
		List<ProgramModel> programs =
				programDateRangeService.getPrograms(startDate, endDate, size, page - 1);

		if (!programs.isEmpty()) {
			return programs.stream()
					.map(
							program -> {
								AttendModel attendModel =
										attendRepository
												.findByProgramIdAndMemberId(program.getId(), memberId)
												.map(attendEntityConverter::from)
												.orElse(null);
								if (attendModel == null) return null;
								return attendInfoWithProgramConverter.from(attendModel, program);
							})
					.filter(Objects::nonNull)
					.toList();
		}

		return Collections.emptyList();
	}

	@Override
	public List<AttendPenaltyResponse> getPenaltyTop10Info() {
		return List.of();
	}

	private void validateParameter(Long startDate, Long endDate, Integer size, Integer page) {
		if (startDate == null || endDate == null || size == null || page == null) {
			throw new IllegalArgumentException();
		}

		if (startDate > endDate) {
			throw new IllegalArgumentException();
		}

		if (size < 0 || page < 0) {
			throw new IllegalArgumentException();
		}

		if (size == 0 || page == 0) {
			throw new IllegalArgumentException();
		}
	}

	private void validateAttend(ProgramModel programModel, AttendModel attendModel) {
		if (programModel.getAttendMode().equals(ProgramAttendMode.END))
			throw new NotStartAttendException();
		if (attendModel.isAttended()) throw new DeniedChangeAttendException();
		if (!attendModel.isRelated()) throw new DeniedSaveAttendException();
	}

	private ProgramModel findProgram(final Long programId) {
		return programRepository
				.findById(programId)
				.map(programEntityConverter::from)
				.orElseThrow(() -> new NotFoundProgramException(programId));
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

		return memberRepository.findMembersByIds(memberIds);
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
			return memberRepository.findMembers();
		}

		return memberRepository.findMembersByActiveStatus(ActiveStatus.find(activeStatus));
	}

	private void validateExistsProgram(Long programId) {
		if (!programRepository.existsById(programId)) {
			throw new NotFoundProgramException(programId);
		}
	}

	private Long calculateRank(Long programId) {
		return attendRepository.countAttendStatusByProgramIdAndStatus(programId, AttendStatus.ATTEND)
				+ 1;
	}
}
