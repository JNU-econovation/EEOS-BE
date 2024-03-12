package com.blackcompany.eeos.target.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.target.application.dto.TargetMember;
import com.blackcompany.eeos.target.fixture.TargetMemberFixture;
import com.blackcompany.eeos.teamBuilding.application.dto.TeamBuildingMember;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SelectCommandTargetMemberServiceTest {
	@Mock private MemberRepository memberRepository;
	@Spy private MemberEntityConverter memberEntityConverter;
	@InjectMocks private SelectAttendCommandTargetMemberService selectTargetService;

	@Test
	@DisplayName("주어진 대상 멤버들로 멤버를 조회한다.")
	void find_members_all() {
		// given
		List<TeamBuildingMember> 수민_바다_팀빌딩_대상자 =
				List.of(TargetMemberFixture.팀빌딩_대상자(1L), TargetMemberFixture.팀빌딩_대상자(2L));
		when(memberRepository.findMembersByIds(
						수민_바다_팀빌딩_대상자.stream()
								.map(TeamBuildingMember::getMemberId)
								.collect(Collectors.toList())))
				.thenReturn(
						List.of(
								MemberFixture.멤버_엔티티(1L, ActiveStatus.AM),
								MemberFixture.멤버_엔티티(2L, ActiveStatus.AM)));

		// when
		List<MemberModel> memberModels = selectTargetService.findMembers(수민_바다_팀빌딩_대상자);

		// then
		assertEquals(memberModels.size(), 2);

		List<Long> 대상자_멤버 =
				수민_바다_팀빌딩_대상자.stream().map(TargetMember::getMemberId).collect(Collectors.toList());
		List<Long> 조회_멤버 =
				memberModels.stream().map(MemberModel::getMemberId).collect(Collectors.toList());

		assertEquals(대상자_멤버, 조회_멤버);
	}

	@Test
	@DisplayName("주어진 대상 멤버들로 모든 멤버들을 조회하지 못 했다.")
	void find_members_not_all() {
		// given
		List<TeamBuildingMember> 수민_바다_팀빌딩_대상자 =
				List.of(TargetMemberFixture.팀빌딩_대상자(1L), TargetMemberFixture.팀빌딩_대상자(2L));
		when(memberRepository.findMembersByIds(
						수민_바다_팀빌딩_대상자.stream()
								.map(TeamBuildingMember::getMemberId)
								.collect(Collectors.toList())))
				.thenReturn(List.of(MemberFixture.멤버_엔티티(1L, ActiveStatus.AM)));

		// when & then
		assertThrows(
				NotFoundMemberException.class, () -> selectTargetService.findMembers(수민_바다_팀빌딩_대상자));
	}
}
