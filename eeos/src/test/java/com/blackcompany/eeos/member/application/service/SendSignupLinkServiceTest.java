package com.blackcompany.eeos.member.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SendSignupLinkServiceTest {

	@Mock MemberRepository memberRepository;
	@Mock SlackDmNotificationService slackDmNotificationService;

	@InjectMocks SendSignupLinkService sendSignupLinkService;

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
}
