package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.model.AttendModel;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.common.persistence.MemberIdEntity;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.program.application.dto.ProgramMembers;
import com.blackcompany.eeos.program.application.model.AttendManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 관련있는 대상자를 지정하여 선택한다. */
@Service
@RequiredArgsConstructor
@Slf4j
public class SelectCandidateService implements CandidateService {
	private final AttendRepository attendRepository;
	private final AttendEntityConverter entityConverter;
	private final MemberRepository memberRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveCandidate(final Long programId, final List<ProgramMembers> members) {
		List<AttendEntity> attendEntities =
				findMembers(members).stream()
						.map(member -> entityConverter.toEntity(member.getId(), programId))
						.collect(Collectors.toList());

		attendRepository.saveAll(attendEntities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCandidate(
			final Long programId, final List<ChangeAttendStatusRequest> requests) {
		List<AttendModel> models =
				findAttends(programId, requests).stream()
						.map(entityConverter::from)
						.collect(Collectors.toList());

		AttendManager attendManager = new AttendManager();
		models.forEach(model -> updateAttendStatus(model, requests, attendManager));

		deleteNonRelated(attendManager);
		updateRelated(attendManager);
	}

	private List<MemberEntity> findMembers(final List<ProgramMembers> members) {
		List<Long> requestMember =
				members.stream().map(ProgramMembers::getMemberId).collect(Collectors.toList());

		List<MemberEntity> findMembers = memberRepository.findUsersByIds(requestMember);
		validateAllFind(requestMember, findMembers);
		return findMembers;
	}

	private List<AttendEntity> findAttends(
			final Long programId, final List<ChangeAttendStatusRequest> members) {
		List<Long> memberIds =
				members.stream().map(ChangeAttendStatusRequest::getMemberId).collect(Collectors.toList());

		List<AttendEntity> attendMembers =
				attendRepository.findAllByProgramMember(programId, memberIds);
		validateAllFind(memberIds, attendMembers);
		return attendMembers;
	}

	private <T extends MemberIdEntity> void validateAllFind(
			List<Long> requestEntities, List<T> findEntities) {
		if (requestEntities.size() == findEntities.size()) {
			return;
		}
		log.error(String.valueOf(findDifferent(requestEntities, findEntities)));
		throw new NotFoundMemberException();
	}

	private <T extends MemberIdEntity> List<Long> findDifferent(
			List<Long> requestEntities, List<T> findEntities) {
		List<Long> findEntitiesId =
				findEntities.stream().map(MemberIdEntity::getId).collect(Collectors.toList());

		requestEntities.removeAll(findEntitiesId);

		return requestEntities;
	}

	private void updateAttendStatus(
			AttendModel model, List<ChangeAttendStatusRequest> requests, AttendManager attendManager) {
		ChangeAttendStatusRequest request = findUpdateRequest(model.getMemberId(), requests);

		validateSameBeforeStatus(model, request);

		if (Objects.equals(request.getAfterAttendStatus(), AttendStatus.NONRELATED.getStatus())) {
			attendManager.addNonRelated(model);
			return;
		}
		model.changeStatus(request.getAfterAttendStatus());
		attendManager.addRelated(model);
	}

	private ChangeAttendStatusRequest findUpdateRequest(
			Long attendId, List<ChangeAttendStatusRequest> requests) {
		return requests.stream()
				.filter(request -> request.getMemberId().equals(attendId))
				.findAny()
				.orElseThrow(NotFoundMemberException::new);
	}

	private void validateSameBeforeStatus(AttendModel model, ChangeAttendStatusRequest request) {
		model.validateSame(request.getBeforeAttendStatus());
	}

	private void deleteNonRelated(AttendManager attendManager) {
		List<AttendEntity> nonRelated =
				attendManager.getNonRelated().stream()
						.map(entityConverter::toEntity)
						.collect(Collectors.toList());

		attendRepository.deleteAll(nonRelated);
	}

	private void updateRelated(AttendManager attendManager) {
		List<AttendEntity> related =
				attendManager.getRelated().stream()
						.map(entityConverter::toEntity)
						.collect(Collectors.toList());

		attendRepository.saveAll(related);
	}
}
