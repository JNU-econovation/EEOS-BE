package com.blackcompany.eeos.notification.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.notification.application.dto.NotificationRequest;
import com.blackcompany.eeos.notification.application.exception.RetryableNotificationException;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.port.NotificationMessage;
import com.blackcompany.eeos.notification.application.port.NotificationResult;
import com.blackcompany.eeos.notification.application.port.NotificationSender;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationSender notificationSender;
	private final MemberPushTokenRepository memberPushTokenRepository;
	private final MemberRepository memberRepository;
	private final NotificationLogRepository notificationLogRepository;
	private final SlackNotificationService slackNotificationService;

	public void sendNotification(NotificationRequest request) {

		List<MemberPushTokenModel> targetTokens = getNotificationTokens();
		List<String> failedTokens = new ArrayList<>();

		for(MemberPushTokenModel token : targetTokens) {
			NotificationMessage message = NotificationMessage.builder()
				.pushToken(token.getPushToken())
				.messageTitle(request.getTitle())
				.messageBody(request.getBody())
				.build();

			boolean success = sendToSingleToken(message);
			if(!success){
				failedTokens.add(token.getPushToken());
			}
		}

		if(!failedTokens.isEmpty()){
			slackNotificationService.sendFailureReport(failedTokens);

		}

	}

	private List<MemberPushTokenModel> getNotificationTokens() {
		List<Long> memberIds = Stream.of(
			memberRepository.findMembersByActiveStatus(ActiveStatus.AM),
			memberRepository.findMembersByActiveStatus(ActiveStatus.CM),
			memberRepository.findMembersByActiveStatus(ActiveStatus.RM)
		).flatMap(List::stream)
			.map(MemberModel::getId)
			.toList();

		return memberPushTokenRepository.findByMemberIdsAndNotificationPermission(memberIds, NotificationPermission.ON);
	}


	@Retryable(
		retryFor = RetryableNotificationException.class,
		maxAttempts = 4,
		backoff = @Backoff(delay = 1000, multiplier = 2)
	)
	public boolean sendToSingleToken(NotificationMessage notificationMessage) {
		//2. notificationSender.send 호출
		NotificationResult result = notificationSender.send(notificationMessage);
		if(result.isSuccess()){
			return true;
		}
		return switch (result.getErrorCode()) {
			case TEMPORARY_ERROR -> throw new RetryableNotificationException(notificationMessage.getPushToken());
			case INVALID_TOKEN -> {
				memberPushTokenRepository.deleteByPushToken(notificationMessage.getPushToken());
				yield false;
			}
			default -> false;
		};

	}

	@Recover
	public boolean recover(RetryableNotificationException e, NotificationMessage message){
		return false;
	}

}
