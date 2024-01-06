package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.dto.ProgramMembers;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** 관련있는 대상자를 지정하여 선택한다. */
@Service
@RequiredArgsConstructor
public class SelectCandidateService implements CandidateService {
	private final AttendRepository attendRepository;
	private final AttendEntityConverter entityConverter;
	private final MemberRepository memberRepository;

	@Override
	public void saveCandidate(final Long programId, final List<ProgramMembers> members) {
		List<AttendEntity> attendEntities =
				findMembers(members).stream()
						.map(member -> entityConverter.toEntity(member.getId(), programId))
						.collect(Collectors.toList());

		attendRepository.saveAll(attendEntities);
	}

	private List<MemberEntity> findMembers(final List<ProgramMembers> members) {
		List<Long> memberIds =
				members.stream().map(ProgramMembers::getMemberId).collect(Collectors.toList());

		return memberRepository.findUsersByIds(memberIds);
	}
}
