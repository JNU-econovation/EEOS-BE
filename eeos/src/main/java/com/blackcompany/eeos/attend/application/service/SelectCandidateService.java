package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
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

	@Override
	public void saveCandidate(final Long programId, final List<ProgramMembers> members) {
		List<AttendEntity> attendEntities =
				members.stream()
						.map(member -> entityConverter.toEntity(member.getMemberId(), programId))
						.collect(Collectors.toList());

		attendRepository.saveAll(attendEntities);
	}
}
