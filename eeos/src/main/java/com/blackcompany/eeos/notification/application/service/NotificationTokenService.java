package com.blackcompany.eeos.notification.application.service;

import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.dto.UpdateNotificationPermissionRequest;
import com.blackcompany.eeos.notification.application.exception.DuplicatePushTokenException;
import com.blackcompany.eeos.notification.application.exception.NotFoundPushTokenException;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.usecase.CreateMemberPushTokenUsecase;
import com.blackcompany.eeos.notification.application.usecase.DeleteAllMemberPushTokensUsecase;
import com.blackcompany.eeos.notification.application.usecase.DeleteMemberPushTokenUsecase;
import com.blackcompany.eeos.notification.application.usecase.UpdateNotificationPermissionUsecase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationTokenService
		implements CreateMemberPushTokenUsecase,
				DeleteAllMemberPushTokensUsecase,
				DeleteMemberPushTokenUsecase,
				UpdateNotificationPermissionUsecase {

	private final MemberPushTokenRepository memberPushTokenRepository;
	private static final int INACTIVE_DAYS_THRESHOLD = 90;

	@Override
	@Transactional
	public void create(Long memberId, CreateMemberPushTokenRequest request) {
		NotificationProvider provider = NotificationProvider.find(request.getProvider());

		MemberPushTokenModel model =
				memberPushTokenRepository
						.findByMemberIdAndPushToken(memberId, request.getPushToken())
						.map(existingModel -> existingModel.renew(memberId))
						.orElseGet(
								() -> {
									if (memberPushTokenRepository
											.findByPushToken(request.getPushToken())
											.isPresent()) {
										throw new DuplicatePushTokenException();
									}
									return MemberPushTokenModel.builder()
											.memberId(memberId)
											.pushToken(request.getPushToken())
											.notificationProvider(provider)
											.lastActiveAt(LocalDateTime.now())
											.notificationPermission(NotificationPermission.ON)
											.build();
								});

		memberPushTokenRepository.save(model);
	}

	@Override
	@Transactional
	public void deleteAllMemberPushTokens(Long memberId) {
		memberPushTokenRepository.deleteByMemberId(memberId);
	}

	@Override
	@Transactional
	public void delete(Long memberId, DeleteMemberPushTokenRequest request) {
		MemberPushTokenModel model =
				memberPushTokenRepository
						.findByPushToken(request.getPushToken())
						.orElseThrow(NotFoundPushTokenException::new);
		model.validateTokenOwner(memberId);
		memberPushTokenRepository.deleteByPushToken(request.getPushToken());
	}

	@Transactional
	public int deleteInactiveTokens() {
		LocalDateTime limitDate = LocalDateTime.now().minusDays(INACTIVE_DAYS_THRESHOLD);
		return memberPushTokenRepository.deleteByLastActiveAtBefore(limitDate);
	}

	@Override
	@Transactional
	public void updateNotificationPermission(
			Long memberId, UpdateNotificationPermissionRequest request) {
		MemberPushTokenModel model =
				memberPushTokenRepository
						.findByPushToken(request.getPushToken())
						.orElseThrow(NotFoundPushTokenException::new);
		model.validateTokenOwner(memberId);
		MemberPushTokenModel updateModel =
				model.updateNotificationPermission(request.getNotificationPermission());
		memberPushTokenRepository.save(updateModel);
	}
}
