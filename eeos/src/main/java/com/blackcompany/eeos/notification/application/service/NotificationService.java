package com.blackcompany.eeos.notification.application.service;

import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.notification.application.dto.NotificationRequest;
import com.blackcompany.eeos.notification.application.dto.NotificationSendResults;
import com.blackcompany.eeos.notification.application.exception.RetryableNotificationException;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import com.blackcompany.eeos.notification.application.port.NotificationErrorCode;
import com.blackcompany.eeos.notification.application.port.NotificationResult;
import com.blackcompany.eeos.notification.application.port.NotificationSender;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.repository.NotificationLogRepository;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationSender notificationSender;
	private final MemberPushTokenRepository memberPushTokenRepository;
	private final MemberRepository memberRepository;
	private final SlackNotificationService slackNotificationService;
	private final RetryTemplate retryTemplate;
	private final NotificationLogRepository notificationLogRepository;
	private static final int BATCH_SIZE = 500;

	public void sendNotification(NotificationRequest request) {

		List<String> targetTokens =
				getNotificationTokens().stream().map(MemberPushTokenModel::getPushToken).toList();
		log.info("알림 전송 시작 - 대상 토큰 수: {}", targetTokens.size());

		if (targetTokens.isEmpty()) {
			log.info("전송 대상 토큰 없음");
			return;
		}

		List<List<String>> partitions = Lists.partition(targetTokens, BATCH_SIZE);
		List<String> failedTokens = new ArrayList<>();

		for (List<String> batch : partitions) {
			List<String> batchFailed = processBatchWithRetry(batch, request);
			failedTokens.addAll(batchFailed);
		}

		if (!failedTokens.isEmpty()) {
			log.warn("최종 실패 토큰 수: {} - 슬랙 알림 전송", failedTokens.size());
			slackNotificationService.sendFailureReport(failedTokens);
		} else {
			log.info("알림 전송 완료 - 모두 성공");
		}
	}

	private List<String> processBatchWithRetry(List<String> tokens, NotificationRequest request) {
		List<String> retryTokens = new ArrayList<>(tokens);
		List<String> permanentlyFailed = new ArrayList<>();

		return retryTemplate.execute(
				context -> {
					Map<String, NotificationResult> results =
							notificationSender.sendAll(request.getTitle(), request.getBody(), retryTokens);

					NotificationSendResults classified = classifyResults(results);

					log.info(
							"시도 #{} - 재시도: {}, 무효: {}, 영구실패: {}",
							context.getRetryCount() + 1,
							classified.getRetryTokens().size(),
							classified.getInvalidTokens().size(),
							classified.getPermanentlyFailedTokens().size());

					retryTokens.clear();
					retryTokens.addAll(classified.getRetryTokens());
					permanentlyFailed.addAll(classified.getPermanentlyFailedTokens());

					saveNotificationLogs(request, classified);
					deleteInvalidTokens(classified.getInvalidTokens());

					if (!retryTokens.isEmpty()) {
						throw new RetryableNotificationException();
					}

					return permanentlyFailed;
				},
				context -> {
					log.error("최대 재시도 초과 - 실패 토큰 수: {}", retryTokens.size());
					retryTokens.forEach(
							token ->
									saveLog(
											request,
											token,
											NotificationStatus.FAILED,
											NotificationErrorCode.TEMPORARY_ERROR));
					permanentlyFailed.addAll(retryTokens);
					return permanentlyFailed;
				});
	}

	private void deleteInvalidTokens(List<String> invalidTokens) {
		if (!invalidTokens.isEmpty()) {
			memberPushTokenRepository.deleteByPushTokenIn(invalidTokens);
		}
	}

	private NotificationSendResults classifyResults(Map<String, NotificationResult> results) {
		List<String> successTokens = new ArrayList<>();
		List<String> retryTokens = new ArrayList<>();
		List<String> invalidTokens = new ArrayList<>();
		List<String> permanentlyFailedTokens = new ArrayList<>();

		results.forEach(
				(token, result) -> {
					if (result.isSuccess()) {
						successTokens.add(token);
					} else if (result.getErrorCode() == NotificationErrorCode.INVALID_TOKEN) {
						invalidTokens.add(token);
					} else if (result.getErrorCode() == NotificationErrorCode.TEMPORARY_ERROR) {
						retryTokens.add(token);
					} else {
						permanentlyFailedTokens.add(token);
					}
				});

		return new NotificationSendResults(
				successTokens, retryTokens, invalidTokens, permanentlyFailedTokens);
	}

	private List<MemberPushTokenModel> getNotificationTokens() {
		List<Long> memberIds =
				Stream.of(
								memberRepository.findMembersByActiveStatus(ActiveStatus.AM),
								memberRepository.findMembersByActiveStatus(ActiveStatus.CM),
								memberRepository.findMembersByActiveStatus(ActiveStatus.RM))
						.flatMap(List::stream)
						.map(MemberModel::getId)
						.toList();

		return memberPushTokenRepository.findByMemberIdsAndNotificationPermission(
				memberIds, NotificationPermission.ON);
	}

	private void saveNotificationLogs(
			NotificationRequest request, NotificationSendResults classifiedResults) {
		classifiedResults
				.getSuccessTokens()
				.forEach(token -> saveLog(request, token, NotificationStatus.SUCCESS, null));

		classifiedResults
				.getInvalidTokens()
				.forEach(
						token ->
								saveLog(
										request,
										token,
										NotificationStatus.FAILED,
										NotificationErrorCode.INVALID_TOKEN));

		classifiedResults
				.getPermanentlyFailedTokens()
				.forEach(
						token ->
								saveLog(
										request,
										token,
										NotificationStatus.FAILED,
										NotificationErrorCode.UNKNOWN_ERROR));
	}

	private void saveLog(
			NotificationRequest request,
			String token,
			NotificationStatus status,
			NotificationErrorCode errorCode) {
		NotificationLogModel logModel =
				NotificationLogModel.builder()
						.calendarId(request.getCalendarId())
						.pushToken(token)
						.calendarType(request.getCalendarType())
						.messageTitle(request.getTitle())
						.messageBody(request.getBody())
						.status(status)
						.errorCode(errorCode)
						.scheduledAt(request.getScheduledAt())
						.sentAt(LocalDateTime.now())
						.provider(notificationSender.getNotificationProvider())
						.build();
		notificationLogRepository.save(logModel);
	}
}
