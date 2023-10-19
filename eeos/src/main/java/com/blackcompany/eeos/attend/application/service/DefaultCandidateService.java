package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
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
@Transactional
public class DefaultCandidateService implements CandidateService {
	private final AttendEntityConverter entityConverter;
	private final MemberService memberService;
	private final AttendRepository attendRepository;

	@Override
	public void saveCandidate(final Long programId) {
		List<AttendEntity> attendEntities =
				memberService.findAllMember().stream()
						.map(memberModel -> entityConverter.toEntity(memberModel.getId(), programId))
						.collect(Collectors.toList());

		attendRepository.saveAll(attendEntities);
	}
}
