package com.blackcompany.eeos.member.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.member.application.dto.SlackSignupDmResponse;
import com.blackcompany.eeos.member.application.usecase.ChangeActiveStatusUsecase;
import com.blackcompany.eeos.member.application.usecase.DepartmentUsecase;
import com.blackcompany.eeos.member.application.usecase.GetMemberByActiveStatus;
import com.blackcompany.eeos.member.application.usecase.GetMembersByActiveStatus;
import com.blackcompany.eeos.member.application.usecase.SendSignupLinkToSlackOnlyMembersUsecase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

	@Mock private ChangeActiveStatusUsecase changeActiveStatusUsecase;
	@Mock private GetMembersByActiveStatus getMembersByActiveStatus;
	@Mock private GetMemberByActiveStatus getMemberByActiveStatus;
	@Mock private DepartmentUsecase departmentUsecase;
	@Mock private SendSignupLinkToSlackOnlyMembersUsecase sendSignupLinkToSlackOnlyMembersUsecase;

	@InjectMocks private MemberController memberController;

	@Test
	@DisplayName("관리자 Slack 회원가입 DM 발송 요청 시 집계 결과를 반환한다.")
	void sendSlackSignupDm_returnsAggregatedResponse() {
		SlackSignupDmResponse response =
				SlackSignupDmResponse.builder().totalCount(12).successCount(11).failCount(1).build();
		when(sendSignupLinkToSlackOnlyMembersUsecase.sendSignupLinks(1L)).thenReturn(response);

		ApiResponse<SuccessBody<SlackSignupDmResponse>> result = memberController.sendSlackSignupDm(1L);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("201", result.getBody().getCode());
		assertEquals(12, result.getBody().getData().getTotalCount());
		assertEquals(11, result.getBody().getData().getSuccessCount());
		assertEquals(1, result.getBody().getData().getFailCount());
		verify(sendSignupLinkToSlackOnlyMembersUsecase).sendSignupLinks(1L);
	}
}
