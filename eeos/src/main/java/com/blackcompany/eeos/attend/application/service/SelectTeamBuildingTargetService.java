package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.TargetMember;
import com.blackcompany.eeos.attend.application.model.converter.TeamBuildingTargetEntityConverter;
import com.blackcompany.eeos.attend.persistence.TeamBuildingTargetEntity;
import com.blackcompany.eeos.attend.persistence.TeamBuildingTargetRepository;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SelectTeamBuildingTargetService extends SelectTargetService implements TargetService {
	private final TeamBuildingTargetEntityConverter entityConverter;
	private final TeamBuildingTargetRepository targetRepository;

	public SelectTeamBuildingTargetService(
			MemberRepository memberRepository,
			MemberEntityConverter memberEntityConverter,
			TeamBuildingTargetEntityConverter entityConverter,
			TeamBuildingTargetRepository targetRepository) {
		super(memberRepository, memberEntityConverter);
		this.entityConverter = entityConverter;
		this.targetRepository = targetRepository;
	}

	@Override
	@Transactional
	public <T extends TargetMember> void save(Long eventId, List<T> members) {
		List<TeamBuildingTargetEntity> targetEntities =
				findMembers(members).stream()
						.map(member -> entityConverter.toEntity(member.getId(), eventId))
						.collect(Collectors.toList());

		targetRepository.saveAll(targetEntities);
	}
}
