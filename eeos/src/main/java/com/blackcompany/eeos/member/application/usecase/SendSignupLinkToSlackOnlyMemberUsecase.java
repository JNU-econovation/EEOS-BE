package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;

public interface SendSignupLinkToSlackOnlyMemberUsecase {
	SlackSignupDmResponse sendSignupLink(Long adminMemberId, Long targetMemberId);
}
