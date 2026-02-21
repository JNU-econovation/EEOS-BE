package com.blackcompany.eeos.notification.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.exception.DuplicatePushTokenException;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class NotificationTokenServiceTest {

	@Mock private MemberPushTokenRepository memberPushTokenRepository;

	private NotificationTokenService notificationTokenService;

	@BeforeEach
	void setUp() {
		notificationTokenService = new NotificationTokenService(memberPushTokenRepository);
	}

	@Test
	@DisplayName("새로운 푸시 토큰을 성공적으로 생성한다")
	void create_new_push_token_success() {
		// given
		Long memberId = 1L;
		CreateMemberPushTokenRequest request =
				CreateMemberPushTokenRequest.builder().pushToken("new-token").provider("FCM").build();

		when(memberPushTokenRepository.findByMemberIdAndPushToken(memberId, "new-token"))
				.thenReturn(Optional.empty());
		when(memberPushTokenRepository.saveAndFlush(any(MemberPushTokenModel.class)))
				.thenReturn(createTokenModel(memberId, "new-token"));

		// when
		notificationTokenService.create(memberId, request);

		// then
		verify(memberPushTokenRepository).saveAndFlush(any(MemberPushTokenModel.class));
	}

	@Test
	@DisplayName("동일한 회원의 기존 토큰이 있으면 갱신한다")
	void create_renews_existing_token_for_same_member() {
		// given
		Long memberId = 1L;
		CreateMemberPushTokenRequest request =
				CreateMemberPushTokenRequest.builder().pushToken("existing-token").provider("FCM").build();

		MemberPushTokenModel existingModel = createTokenModel(memberId, "existing-token");
		when(memberPushTokenRepository.findByMemberIdAndPushToken(memberId, "existing-token"))
				.thenReturn(Optional.of(existingModel));
		when(memberPushTokenRepository.saveAndFlush(any(MemberPushTokenModel.class)))
				.thenReturn(existingModel);

		// when
		notificationTokenService.create(memberId, request);

		// then
		verify(memberPushTokenRepository).saveAndFlush(any(MemberPushTokenModel.class));
	}

	@Test
	@DisplayName("다른 회원이 이미 등록한 토큰으로 생성 시도하면 DuplicatePushTokenException이 발생한다")
	void create_throws_exception_when_token_belongs_to_another_member() {
		// given
		Long memberId = 1L;
		CreateMemberPushTokenRequest request =
				CreateMemberPushTokenRequest.builder().pushToken("duplicate-token").provider("FCM").build();

		when(memberPushTokenRepository.findByMemberIdAndPushToken(memberId, "duplicate-token"))
				.thenReturn(Optional.empty());
		when(memberPushTokenRepository.saveAndFlush(any(MemberPushTokenModel.class)))
				.thenThrow(new DataIntegrityViolationException("Duplicate entry"));

		// when & then
		assertThatThrownBy(() -> notificationTokenService.create(memberId, request))
				.isInstanceOf(DuplicatePushTokenException.class);
	}

	@Test
	@DisplayName("동시에 같은 토큰으로 생성 시도 시 TOCTOU 문제가 발생하지 않고 예외가 발생한다")
	void create_handles_concurrent_duplicate_token_creation() {
		// given
		Long memberIdA = 1L;
		Long memberIdB = 2L;
		String sameToken = "same-token";

		CreateMemberPushTokenRequest requestA =
				CreateMemberPushTokenRequest.builder().pushToken(sameToken).provider("FCM").build();

		when(memberPushTokenRepository.findByMemberIdAndPushToken(memberIdA, sameToken))
				.thenReturn(Optional.empty());
		when(memberPushTokenRepository.saveAndFlush(any(MemberPushTokenModel.class)))
				.thenThrow(new DataIntegrityViolationException("Duplicate entry"));

		// when & then
		assertThatThrownBy(() -> notificationTokenService.create(memberIdA, requestA))
				.isInstanceOf(DuplicatePushTokenException.class);

		verify(memberPushTokenRepository).saveAndFlush(any(MemberPushTokenModel.class));
	}

	private MemberPushTokenModel createTokenModel(Long memberId, String pushToken) {
		return MemberPushTokenModel.builder()
				.id(1L)
				.memberId(memberId)
				.pushToken(pushToken)
				.notificationProvider(NotificationProvider.FCM)
				.notificationPermission(NotificationPermission.ON)
				.lastActiveAt(LocalDateTime.now())
				.build();
	}
}
