package com.blackcompany.eeos.notification.application.usecase;

import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;
import org.springframework.stereotype.Component;

@Component
public interface DeleteMemberPushTokenUsecase {
	void delete(Long memberId, DeleteMemberPushTokenRequest request);
}
