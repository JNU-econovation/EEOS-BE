package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.target.application.dto.TargetMember;
import com.blackcompany.eeos.target.application.model.converter.TeamBuildingTargetEntityConverter;
import com.blackcompany.eeos.target.application.service.CommandTargetMemberService;
import com.blackcompany.eeos.target.application.service.SelectTargetMemberService;
import com.blackcompany.eeos.target.persistence.teambuilding.TeamBuildingTargetEntity;
import com.blackcompany.eeos.target.persistence.teambuilding.TeamBuildingTargetRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommandTeamBuildingTargetMemberService extends SelectTargetMemberService
		implements CommandTargetMemberService {
	private final TeamBuildingTargetEntityConverter entityConverter;
	private final TeamBuildingTargetRepository targetRepository;

	public CommandTeamBuildingTargetMemberService(
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
						.map(member -> entityConverter.toEntity(eventId, member.getMemberId()))
						.toList();

		targetRepository.saveAll(targetEntities);
	}
}
