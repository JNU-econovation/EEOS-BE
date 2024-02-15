package com.blackcompany.eeos.attend.fixture;

import com.blackcompany.eeos.program.application.dto.ProgramMembers;
import com.blackcompany.eeos.teamBuilding.application.dto.TeamBuildingMember;
import java.util.List;

public class TargetMemberFixture {
	public static List<TeamBuildingMember> 수민_바다_팀빌딩_대상자() {
		return List.of(수민_팀빌딩_대상자(), 바다_팀빌딩_대상자());
	}

	public static TeamBuildingMember 수민_팀빌딩_대상자() {
		return TeamBuildingMember.builder().memberId(1L).build();
	}

	public static TeamBuildingMember 바다_팀빌딩_대상자() {
		return TeamBuildingMember.builder().memberId(2L).build();
	}

	public static List<ProgramMembers> 수민_바다_프로그램_대상자() {
		return List.of(수민_프로그램_대상자(), 바다_프로그램_대상자());
	}

	public static ProgramMembers 수민_프로그램_대상자() {
		return ProgramMembers.builder().memberId(1L).build();
	}

	public static ProgramMembers 바다_프로그램_대상자() {
		return ProgramMembers.builder().memberId(2L).build();
	}
}
