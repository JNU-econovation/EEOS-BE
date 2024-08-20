package com.blackcompany.eeos.member.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.member.application.dto.*;
import com.blackcompany.eeos.member.application.usecase.*;
import com.blackcompany.eeos.member.presentation.docs.MemberApi;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController implements MemberApi {
	private final ChangeActiveStatusUsecase changeActiveStatusUsecase;
	private final GetMembersByActiveStatus getMembersByActiveStatus;
	private final GetMemberByActiveStatus getMemberByActiveStatus;

	@Override
	@PutMapping("/activeStatus/{memberId}")
	public ApiResponse<SuccessBody<CommandMemberResponse>> adminChangeActiveStatus(
			@Member Long adminMemberId,
			@PathVariable("memberId") Long memberId,
			@RequestBody @Valid ChangeActiveStatusRequest request) {
		CommandMemberResponse response =
				changeActiveStatusUsecase.adminChangeStatus(adminMemberId, memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Override
	@DeleteMapping("/{memberId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long adminMemberId, @PathVariable("memberId") Long memberId) {
		changeActiveStatusUsecase.delete(adminMemberId, memberId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@GetMapping
	public ApiResponse<SuccessBody<QueryMembersResponse>> findMembersByActiveStatus(
			@RequestParam("activeStatus") String activeStatus) {
		QueryMembersResponse response = getMembersByActiveStatus.execute(activeStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/activeStatus")
	public ApiResponse<SuccessBody<QueryMemberResponse>> findMemberByActiveStatus(
			@Member Long memberId) {
		QueryMemberResponse response = getMemberByActiveStatus.execute(memberId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
