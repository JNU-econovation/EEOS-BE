package com.blackcompany.eeos.notification.application.service;

import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.exception.DeniedDeletePushTokenException;
import com.blackcompany.eeos.notification.application.exception.NotFoundPushTokenException;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.usecase.CreateMemberPushTokenUsecase;
import com.blackcompany.eeos.notification.application.usecase.DeleteAllMemberPushTokensUsecase;
import com.blackcompany.eeos.notification.application.usecase.DeleteMemberPushTokenUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationTokenService
		implements CreateMemberPushTokenUsecase,
				DeleteAllMemberPushTokensUsecase,
				DeleteMemberPushTokenUsecase {

	private final MemberPushTokenRepository memberPushTokenRepository;

	@Override
	@Transactional
	public void create(Long memberId, CreateMemberPushTokenRequest request) {

		NotificationProvider provider = NotificationProvider.find(request.getProvider());
		if (memberPushTokenRepository.findByPushToken(request.getPushToken()).isEmpty()) {
			MemberPushTokenModel model =
					MemberPushTokenModel.builder()
							.memberId(memberId)
							.pushToken(request.getPushToken())
							.notificationProvider(provider)
							.build();

			memberPushTokenRepository.save(model);
		}
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
		if (!model.getMemberId().equals(memberId)) {
			throw new DeniedDeletePushTokenException();
		}
		memberPushTokenRepository.deleteByPushToken(request.getPushToken());
	}
}
