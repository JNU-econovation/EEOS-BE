package com.blackcompany.eeos.notification.application.scheduler;

import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.service.SlackNotificationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PushTokenCleanScheduler {

	private final MemberPushTokenRepository memberPushTokenRepository;
	private final SlackNotificationService slackNotificationService;

	private static final int INACTIVE_DAYS_THRESHOLD = 90;
	private static final String SCHEDULER_NAME = "비활성화 토큰 삭제 스케줄러";

	/*
	 * 매주 토요일 새벽 3시 90일 이상 비활성화된 푸시 토큰 삭제
	 * cron : 초 분 시 일 월 요일(6=토요일)
	 * */

	@Scheduled(cron = "0 0 3 * * 6")
	@Transactional
	@Retryable(
			maxAttempts = 3,
			backoff = @Backoff(delay = 2000),
			recover = "recoverDeleteInactiveTokens")
	public void deleteInactiveTokens() {
		log.info("{} 시작", SCHEDULER_NAME);
		LocalDateTime limitDate = LocalDateTime.now().minusDays(INACTIVE_DAYS_THRESHOLD);
		int deleteCount = memberPushTokenRepository.deleteByLastActiveAtBefore(limitDate);
		log.info("{}개의 비활성 푸시 토큰 삭제 완료 (기준일: {})", deleteCount, limitDate);
	}

	@Recover
	public void recoverDeleteInactiveTokens(Exception e) {
		log.error("{} 실행 실패 - 모든 재시도 소진", SCHEDULER_NAME, e);

		String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();

		if (errorMessage.length() > 300) {
			errorMessage = errorMessage.substring(0, 300) + "...생략";
		}
		slackNotificationService.sendSchedulerFailureMessage(SCHEDULER_NAME, errorMessage);
	}
}
