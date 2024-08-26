package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.common.application.model.MemberIdModel;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.dto.ChangeAllAttendStatusRequest;
import com.blackcompany.eeos.program.application.model.AttendManager;
import com.blackcompany.eeos.target.application.dto.TargetMember;
import com.blackcompany.eeos.target.application.model.AttendModel;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.target.persistence.AttendEntity;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SelectAttendCommandTargetMemberMemberService extends SelectTargetMemberService
		implements CommandTargetMemberService, AttendTargetService {
	private final AttendRepository attendRepository;
	private final AttendEntityConverter entityConverter;

	public SelectAttendCommandTargetMemberMemberService(
			MemberRepository memberRepository,
			MemberEntityConverter memberEntityConverter,
			AttendRepository attendRepository,
			AttendEntityConverter entityConverter) {
		super(memberRepository, memberEntityConverter);
		this.attendRepository = attendRepository;
		this.entityConverter = entityConverter;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends TargetMember> void save(Long eventId, List<T> members) {
		List<AttendEntity> attendEntities =
				findMembers(members).stream()
						.map(member -> entityConverter.toEntity(member.getId(), eventId))
						.collect(Collectors.toList());

		attendRepository.saveAll(attendEntities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(final Long programId, final List<ChangeAllAttendStatusRequest> requests) {
		List<AttendModel> requestAttends = findAttends(programId, requests);

		AttendManager attendManager = new AttendManager();
		requestAttends.forEach(attend -> updateAttendStatus(attend, requests, attendManager));

		deleteNonRelated(attendManager);
		updateRelated(attendManager);
	}

	private List<AttendModel> findAttends(
			final Long programId, final List<ChangeAllAttendStatusRequest> requests) {
		List<Long> memberIds =
				requests.stream()
						.map(ChangeAllAttendStatusRequest::getMemberId)
						.collect(Collectors.toList());

		List<AttendModel> existingAttends = findExistingAttends(programId, memberIds);
		List<AttendModel> notExistingAttends =
				findNotExistingAttends(memberIds, existingAttends, programId);

		return Stream.concat(existingAttends.stream(), notExistingAttends.stream())
				.collect(Collectors.toList());
	}

	private List<AttendModel> findExistingAttends(Long programId, List<Long> memberIds) {
		return attendRepository.findAllByProgramMember(programId, memberIds).stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}

	private List<AttendModel> findNotExistingAttends(
			final List<Long> memberIds, final List<AttendModel> existingAttends, final Long programId) {
		List<Long> notExistingAttendMemberIds = findDifferent(memberIds, existingAttends);
		return AttendModel.of(notExistingAttendMemberIds, programId);
	}

	private <T extends MemberIdModel> List<Long> findDifferent(
			List<Long> requestIds, List<T> findModels) {
		List<Long> findModelIds =
				findModels.stream().map(MemberIdModel::getMemberId).collect(Collectors.toList());
		requestIds.removeAll(findModelIds);
		return requestIds;
	}

	private void updateAttendStatus(
			AttendModel model, List<ChangeAllAttendStatusRequest> requests, AttendManager attendManager) {
		ChangeAllAttendStatusRequest request = findUpdateRequest(model.getMemberId(), requests);
		model.changeStatus(request.getAfterAttendStatus());

		if (Objects.equals(request.getAfterAttendStatus(), AttendStatus.NONRELATED.getStatus())) {
			attendManager.addNonRelated(model);
			return;
		}

		attendManager.addRelated(model);
	}

	private ChangeAllAttendStatusRequest findUpdateRequest(
			Long attendId, List<ChangeAllAttendStatusRequest> requests) {
		return requests.stream()
				.filter(request -> request.getMemberId().equals(attendId))
				.findAny()
				.orElseThrow(NotFoundMemberException::new);
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
