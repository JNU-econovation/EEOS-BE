package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;

public interface SendSignupLinkToSlackOnlyMembersUsecase {
	SlackSignupDmResponse sendSignupLinks(Long adminMemberId);
}
