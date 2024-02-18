package com.blackcompany.eeos.attend.fixture;

import com.blackcompany.eeos.program.application.dto.ProgramMembers;
import com.blackcompany.eeos.teamBuilding.application.dto.TeamBuildingMember;

public class TargetMemberFixture {

	public static TeamBuildingMember 팀빌딩_대상자(Long memberId) {
		return TeamBuildingMember.builder().memberId(memberId).build();
	}

	public static ProgramMembers 프로그램_대상자(Long memberId) {
		return ProgramMembers.builder().memberId(memberId).build();
	}
}
