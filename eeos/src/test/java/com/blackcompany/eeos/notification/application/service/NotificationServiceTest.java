package com.blackcompany.eeos.notification.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.notification.application.dto.NotificationRequest;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.port.NotificationErrorCode;
import com.blackcompany.eeos.notification.application.port.NotificationResult;
import com.blackcompany.eeos.notification.application.port.NotificationSender;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.repository.NotificationLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.support.RetryTemplate;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock private NotificationSender notificationSender;
	@Mock private MemberPushTokenRepository memberPushTokenRepository;
	@Mock private MemberRepository memberRepository;
	@Mock private SlackNotificationService slackNotificationService;
	@Mock private NotificationLogRepository notificationLogRepository;

	private NotificationService notificationService;
	private RetryTemplate retryTemplate;

	@BeforeEach
	void setUp() {
		retryTemplate = RetryTemplate.builder().maxAttempts(3).fixedBackoff(100).build();

		notificationService =
				new NotificationService(
						notificationSender,
						memberPushTokenRepository,
						memberRepository,
						slackNotificationService,
						retryTemplate,
						notificationLogRepository);
	}

	@Test
	@DisplayName("전송 대상 토큰이 없으면 알림을 보내지 않는다")
	void no_tokens_no_notification() {
		// given
		when(memberRepository.findMembersByActiveStatus(any())).thenReturn(List.of());
		when(memberPushTokenRepository.findByMemberIdsAndNotificationPermission(
						anyList(), any(NotificationPermission.class)))
				.thenReturn(List.of());

		NotificationRequest request = createTestRequest();

		// when
		notificationService.sendNotification(request);

		// then
		verify(notificationSender, never()).sendAll(anyString(), anyString(), anyList());
	}

	@Test
	@DisplayName("모든 알림이 성공하면 슬랙 알림을 보내지 않는다")
	void all_success_no_slack() {
		// given
		setupMockMembers();
		setupMockTokens("token1", "token2");

		when(notificationSender.sendAll(anyString(), anyString(), anyList()))
				.thenReturn(
						Map.of(
								"token1", NotificationResult.success(),
								"token2", NotificationResult.success()));
		when(notificationSender.getNotificationProvider()).thenReturn(NotificationProvider.FCM);

		NotificationRequest request = createTestRequest();

		// when
		notificationService.sendNotification(request);

		// then
		verify(slackNotificationService, never()).sendFailureReport(anyList());
	}

	@Test
	@DisplayName("영구 실패 토큰이 있으면 슬랙 알림을 보낸다")
	void permanent_failure_sends_slack() {
		// given
		setupMockMembers();
		setupMockTokens("token1");

		when(notificationSender.sendAll(anyString(), anyString(), anyList()))
				.thenReturn(Map.of("token1", NotificationResult.fail(NotificationErrorCode.UNKNOWN_ERROR)));
		when(notificationSender.getNotificationProvider()).thenReturn(NotificationProvider.FCM);

		NotificationRequest request = createTestRequest();

		// when
		notificationService.sendNotification(request);

		// then
		verify(slackNotificationService).sendFailureReport(anyList());
	}

	@Test
	@DisplayName("유효하지 않은 토큰은 삭제된다")
	void invalid_tokens_deleted() {
		// given
		setupMockMembers();
		setupMockTokens("validToken", "invalidToken");

		when(notificationSender.sendAll(anyString(), anyString(), anyList()))
				.thenReturn(
						Map.of(
								"validToken", NotificationResult.success(),
								"invalidToken", NotificationResult.fail(NotificationErrorCode.INVALID_TOKEN)));
		when(notificationSender.getNotificationProvider()).thenReturn(NotificationProvider.FCM);

		NotificationRequest request = createTestRequest();

		// when
		notificationService.sendNotification(request);

		// then
		verify(memberPushTokenRepository).deleteByPushTokenIn(List.of("invalidToken"));
	}

	@Test
	@DisplayName("알림 전송 결과가 로그에 저장된다")
	void notification_log_saved() {
		// given
		setupMockMembers();
		setupMockTokens("token1");

		when(notificationSender.sendAll(anyString(), anyString(), anyList()))
				.thenReturn(Map.of("token1", NotificationResult.success()));
		when(notificationSender.getNotificationProvider()).thenReturn(NotificationProvider.FCM);

		NotificationRequest request = createTestRequest();

		// when
		notificationService.sendNotification(request);

		// then
		verify(notificationLogRepository, atLeastOnce()).save(any());
	}

	@Test
	@DisplayName("AM, CM, RM 활성 멤버의 토큰만 대상으로 한다")
	void only_active_members_targeted() {
		// given
		MemberModel amMember = createMemberModel(1L);
		MemberModel cmMember = createMemberModel(2L);
		MemberModel rmMember = createMemberModel(3L);

		when(memberRepository.findMembersByActiveStatus(ActiveStatus.AM)).thenReturn(List.of(amMember));
		when(memberRepository.findMembersByActiveStatus(ActiveStatus.CM)).thenReturn(List.of(cmMember));
		when(memberRepository.findMembersByActiveStatus(ActiveStatus.RM)).thenReturn(List.of(rmMember));
		when(memberPushTokenRepository.findByMemberIdsAndNotificationPermission(
						anyList(), any(NotificationPermission.class)))
				.thenReturn(List.of());

		NotificationRequest request = createTestRequest();

		// when
		notificationService.sendNotification(request);

		// then
		verify(memberRepository).findMembersByActiveStatus(ActiveStatus.AM);
		verify(memberRepository).findMembersByActiveStatus(ActiveStatus.CM);
		verify(memberRepository).findMembersByActiveStatus(ActiveStatus.RM);
	}

	private NotificationRequest createTestRequest() {
		return NotificationRequest.builder()
				.calendarId(1L)
				.calendarType(CalendarType.EVENT)
				.title("테스트 제목")
				.body("테스트 본문")
				.scheduledAt(LocalDateTime.now())
				.build();
	}

	private void setupMockMembers() {
		MemberModel member = createMemberModel(1L);
		when(memberRepository.findMembersByActiveStatus(any())).thenReturn(List.of(member));
	}

	private void setupMockTokens(String... tokens) {
		List<MemberPushTokenModel> tokenModels =
				java.util.Arrays.stream(tokens)
						.map(
								token ->
										MemberPushTokenModel.builder()
												.id(1L)
												.memberId(1L)
												.pushToken(token)
												.notificationProvider(NotificationProvider.FCM)
												.notificationPermission(NotificationPermission.ON)
												.build())
						.toList();

		when(memberPushTokenRepository.findByMemberIdsAndNotificationPermission(
						anyList(), any(NotificationPermission.class)))
				.thenReturn(tokenModels);
	}

	private MemberModel createMemberModel(Long id) {
		return MemberModel.builder().id(id).activeStatus(ActiveStatus.AM).build();
	}
}
