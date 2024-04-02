package com.blackcompany.eeos.member.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.dto.QueryMembersResponse;
import com.blackcompany.eeos.member.application.usecase.GetMemberByActiveStatus;
import com.blackcompany.eeos.member.application.usecase.GetMembersByActiveStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QueryMemberController {
	private final GetMembersByActiveStatus getMembersByActiveStatus;
	private final GetMemberByActiveStatus getMemberByActiveStatus;

	@Operation(summary = "활동 상태와 일치하는 회원 리스트를 가져온다.", description = "Request Parameter 값으로 전달 받은 활동 상태와 동일한 회원 리스트를 가져온다.")
	@GetMapping("/members")
	public ApiResponse<SuccessBody<QueryMembersResponse>> findMembersByActiveStatus(
			@RequestParam("activeStatus") String activeStatus) {
		QueryMembersResponse response = getMembersByActiveStatus.execute(activeStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Operation(summary = "본인(회원 1인)의 활동 상태 가져오기", description = "액세스 토큰에서 회원 ID를 추출하여 회원 상태를 가져온다.")
	@GetMapping("/members/activeStatus")
	public ApiResponse<SuccessBody<QueryMemberResponse>> findMemberByActiveStatus(
			@Member Long memberId) {
		QueryMemberResponse response = getMemberByActiveStatus.execute(memberId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
