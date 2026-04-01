package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;
import com.blackcompany.eeos.member.application.exception.DeniedMemberEditException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.application.usecase.SendSignupLinkToSlackOnlyMembersUsecase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SendSignupLinkService implements SendSignupLinkToSlackOnlyMembersUsecase {

	private final MemberRepository memberRepository;
	private final SlackDmNotificationService slackDmNotificationService;

	private String signupUrl = "https://eeos.econovation.kr/signup";

	@Value("${eeos.signup.url:https://eeos.econovation.kr/signup}")
	public void setSignupUrl(String signupUrl) {
		this.signupUrl = signupUrl;
	}

	@Override
	public SlackSignupDmResponse sendSignupLinks(Long adminMemberId) {
		validateAdminPermission(adminMemberId);
		List<MemberModel> targets = memberRepository.findSlackOnlyMembers();

		int totalCount = targets.size();
		int successCount = 0;
		int failCount = 0;

		for (MemberModel member : targets) {
			try {
				slackDmNotificationService.sendSignupLink(member, signupUrl);
				successCount++;
			} catch (Exception e) {
				log.warn(
						"Slack DM 발송 실패. memberId={}, memberName={}, error={}",
						member.getId(),
						member.getName(),
						e.getMessage(),
						e);
				failCount++;
			}
		}

		return SlackSignupDmResponse.builder()
				.totalCount(totalCount)
				.successCount(successCount)
				.failCount(failCount)
				.build();
	}

	private void validateAdminPermission(Long memberId) {
		MemberModel member = memberRepository.findById(memberId);
		if (!member.isAdmin()) {
			throw new DeniedMemberEditException(memberId);
		}
	}
}
