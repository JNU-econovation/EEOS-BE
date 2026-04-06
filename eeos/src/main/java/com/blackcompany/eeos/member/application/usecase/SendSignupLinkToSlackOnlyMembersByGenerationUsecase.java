package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;

public interface SendSignupLinkToSlackOnlyMembersByGenerationUsecase {
	SlackSignupDmResponse sendSignupLinksByGeneration(Long adminMemberId, int generation);
}
