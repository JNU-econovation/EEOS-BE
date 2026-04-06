package com.blackcompany.eeos.member.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;
import com.blackcompany.eeos.member.application.exception.DeniedMemberEditException;
import com.blackcompany.eeos.member.application.exception.NotSlackOnlyMemberException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SendSignupLinkServiceTest {

	@Mock MemberRepository memberRepository;
	@Mock SlackDmNotificationService slackDmNotificationService;

	SendSignupLinkService sendSignupLinkService;

	@BeforeEach
	void setUp() {
		sendSignupLinkService =
				new SendSignupLinkService(
						memberRepository, slackDmNotificationService, "https://auth.econovation.kr");
	}

	@Test
	@DisplayName("Slack Only 회원이 0명일 때 DM 발송 없이 totalCount=0 결과를 반환한다.")
	void sendDm_noSlackOnlyMembers_returnZeroCount() {
		// given
		when(memberRepository.findById(1L)).thenReturn(MemberFixture.어드민_모델(1L));
		when(memberRepository.findSlackOnlyMembers()).thenReturn(Collections.emptyList());

		// when
		SlackSignupDmResponse result = sendSignupLinkService.sendSignupLinks(1L);

		// then
		assertEquals(0, result.getTotalCount());
		assertEquals(0, result.getSuccessCount());
		assertEquals(0, result.getFailCount());
		verify(slackDmNotificationService, never()).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("Slack Only 회원이 있을 때 successCount가 대상 수와 같아야 한다.")
	void sendDm_allSuccess_successCountEqualsTargetSize() {
		// given
		List<MemberModel> targets =
				List.of(MemberFixture.슬랙온리_모델(2L, "U_SLACK_001"), MemberFixture.슬랙온리_모델(3L, "U_SLACK_002"));

		when(memberRepository.findById(1L)).thenReturn(MemberFixture.어드민_모델(1L));
		when(memberRepository.findSlackOnlyMembers()).thenReturn(targets);

		// when
		SlackSignupDmResponse result = sendSignupLinkService.sendSignupLinks(1L);

		// then
		assertEquals(2, result.getTotalCount());
		assertEquals(2, result.getSuccessCount());
		assertEquals(0, result.getFailCount());
		verify(slackDmNotificationService, times(2)).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("DM 발송 중 일부 실패 시 failCount가 증가하고 전체 발송이 중단되지 않는다.")
	void sendDm_partialFailure_failCountIncreased_noAbort() {
		// given
		MemberModel member1 = MemberFixture.슬랙온리_모델(2L, "U_SLACK_001");
		MemberModel member2 = MemberFixture.슬랙온리_모델(3L, "U_SLACK_002");
		MemberModel member3 = MemberFixture.슬랙온리_모델(4L, "U_SLACK_003");

		List<MemberModel> targets = List.of(member1, member2, member3);
		when(memberRepository.findById(1L)).thenReturn(MemberFixture.어드민_모델(1L));
		when(memberRepository.findSlackOnlyMembers()).thenReturn(targets);

		doThrow(new RuntimeException("Slack API error"))
				.when(slackDmNotificationService)
				.sendSignupLink(eq(member2), anyString());

		// when
		SlackSignupDmResponse result = sendSignupLinkService.sendSignupLinks(1L);

		// then
		assertEquals(3, result.getTotalCount());
		assertEquals(2, result.getSuccessCount());
		assertEquals(1, result.getFailCount());
		verify(slackDmNotificationService, times(3)).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("Slack Only 회원에게 단건 DM 발송 성공 시 successCount=1 결과를 반환한다.")
	void sendSignupLink_성공_슬랙온리회원에게_DM발송_성공() {
		// given
		Long adminMemberId = 1L;
		Long targetMemberId = 2L;
		MemberModel admin = MemberFixture.어드민_모델(adminMemberId);
		MemberModel target = MemberFixture.슬랙온리_모델(targetMemberId, "U_SLACK_001");

		when(memberRepository.findById(adminMemberId)).thenReturn(admin);
		when(memberRepository.findById(targetMemberId)).thenReturn(target);

		// when
		SlackSignupDmResponse result =
				sendSignupLinkService.sendSignupLink(adminMemberId, targetMemberId);

		// then
		assertEquals(1, result.getTotalCount());
		assertEquals(1, result.getSuccessCount());
		assertEquals(0, result.getFailCount());
		verify(slackDmNotificationService, times(1)).sendSignupLink(eq(target), anyString());
	}

	@Test
	@DisplayName("대상 회원이 Slack Only가 아닌 경우 NotSlackOnlyMemberException이 발생한다.")
	void sendSignupLink_실패_슬랙온리아닌회원_예외발생() {
		// given
		Long adminMemberId = 1L;
		Long targetMemberId = 3L;
		MemberModel admin = MemberFixture.어드민_모델(adminMemberId);
		MemberModel nonSlackOnlyMember = MemberFixture.비슬랙온리_모델(targetMemberId);

		when(memberRepository.findById(adminMemberId)).thenReturn(admin);
		when(memberRepository.findById(targetMemberId)).thenReturn(nonSlackOnlyMember);

		// when & then
		assertThrows(
				NotSlackOnlyMemberException.class,
				() -> sendSignupLinkService.sendSignupLink(adminMemberId, targetMemberId));
		verify(slackDmNotificationService, never()).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("단건 DM 발송 중 슬랙 API 예외 발생 시 failCount=1 결과를 반환한다.")
	void sendSignupLink_실패_DM발송_예외_failCount증가() {
		// given
		Long adminMemberId = 1L;
		Long targetMemberId = 2L;
		MemberModel admin = MemberFixture.어드민_모델(adminMemberId);
		MemberModel target = MemberFixture.슬랙온리_모델(targetMemberId, "U_SLACK_001");

		when(memberRepository.findById(adminMemberId)).thenReturn(admin);
		when(memberRepository.findById(targetMemberId)).thenReturn(target);
		doThrow(new RuntimeException("Slack API error"))
				.when(slackDmNotificationService)
				.sendSignupLink(eq(target), anyString());

		// when
		SlackSignupDmResponse result =
				sendSignupLinkService.sendSignupLink(adminMemberId, targetMemberId);

		// then
		assertEquals(1, result.getTotalCount());
		assertEquals(0, result.getSuccessCount());
		assertEquals(1, result.getFailCount());
	}

	// =====================================================================
	// 기수별 Slack 회원가입 DM 발송 테스트
	// =====================================================================

	@Test
	@DisplayName("해당 기수에 Slack 전용 회원이 있을 때 해당 인원에게 DM을 성공적으로 발송한다.")
	void sendSignupLinksByGeneration_성공_해당기수_슬랙온리회원_DM발송() {
		// given
		int generation = 12;
		Long adminMemberId = 1L;
		MemberModel admin = MemberFixture.어드민_모델(adminMemberId);
		List<MemberModel> targets =
				List.of(
						MemberFixture.기수별_슬랙온리_모델(2L, generation, "홍길동"),
						MemberFixture.기수별_슬랙온리_모델(3L, generation, "김철수"));

		when(memberRepository.findById(adminMemberId)).thenReturn(admin);
		when(memberRepository.findSlackOnlyMembersByGeneration(generation)).thenReturn(targets);

		// when
		SlackSignupDmResponse result =
				sendSignupLinkService.sendSignupLinksByGeneration(adminMemberId, generation);

		// then
		assertEquals(2, result.getTotalCount());
		assertEquals(2, result.getSuccessCount());
		assertEquals(0, result.getFailCount());
		verify(slackDmNotificationService, times(2)).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("해당 기수에 Slack 전용 회원이 없을 때 DM 발송 없이 totalCount=0 결과를 반환한다.")
	void sendSignupLinksByGeneration_성공_대상없음_빈결과반환() {
		// given
		int generation = 99;
		Long adminMemberId = 1L;
		MemberModel admin = MemberFixture.어드민_모델(adminMemberId);

		when(memberRepository.findById(adminMemberId)).thenReturn(admin);
		when(memberRepository.findSlackOnlyMembersByGeneration(generation))
				.thenReturn(Collections.emptyList());

		// when
		SlackSignupDmResponse result =
				sendSignupLinkService.sendSignupLinksByGeneration(adminMemberId, generation);

		// then
		assertEquals(0, result.getTotalCount());
		assertEquals(0, result.getSuccessCount());
		assertEquals(0, result.getFailCount());
		verify(slackDmNotificationService, never()).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("기수별 DM 발송 중 일부 Slack API 호출이 실패하면 failCount가 증가하고 나머지 발송은 계속된다.")
	void sendSignupLinksByGeneration_부분실패_failCount증가_발송중단없음() {
		// given
		int generation = 12;
		Long adminMemberId = 1L;
		MemberModel admin = MemberFixture.어드민_모델(adminMemberId);
		MemberModel member1 = MemberFixture.기수별_슬랙온리_모델(2L, generation, "홍길동");
		MemberModel member2 = MemberFixture.기수별_슬랙온리_모델(3L, generation, "김철수");
		MemberModel member3 = MemberFixture.기수별_슬랙온리_모델(4L, generation, "이영희");

		when(memberRepository.findById(adminMemberId)).thenReturn(admin);
		when(memberRepository.findSlackOnlyMembersByGeneration(generation))
				.thenReturn(List.of(member1, member2, member3));
		doThrow(new RuntimeException("Slack API error"))
				.when(slackDmNotificationService)
				.sendSignupLink(eq(member2), anyString());

		// when
		SlackSignupDmResponse result =
				sendSignupLinkService.sendSignupLinksByGeneration(adminMemberId, generation);

		// then
		assertEquals(3, result.getTotalCount());
		assertEquals(2, result.getSuccessCount());
		assertEquals(1, result.getFailCount());
		verify(slackDmNotificationService, times(3)).sendSignupLink(any(), anyString());
	}

	@Test
	@DisplayName("관리자가 아닌 회원이 기수별 DM 발송을 요청하면 DeniedMemberEditException이 발생한다.")
	void sendSignupLinksByGeneration_실패_관리자아닌회원_예외발생() {
		// given
		int generation = 12;
		Long nonAdminMemberId = 5L;
		MemberModel nonAdmin =
				MemberFixture.멤버_모델(
						nonAdminMemberId, com.blackcompany.eeos.member.application.model.ActiveStatus.AM);

		when(memberRepository.findById(nonAdminMemberId)).thenReturn(nonAdmin);

		// when & then
		assertThrows(
				DeniedMemberEditException.class,
				() -> sendSignupLinkService.sendSignupLinksByGeneration(nonAdminMemberId, generation));
		verify(slackDmNotificationService, never()).sendSignupLink(any(), anyString());
	}
}
