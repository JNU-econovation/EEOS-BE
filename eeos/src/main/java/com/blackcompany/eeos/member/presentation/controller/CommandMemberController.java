package com.blackcompany.eeos.member.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;
import com.blackcompany.eeos.member.application.usecase.ChangeActiveStatusUsecase;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class CommandMemberController {
	private final ChangeActiveStatusUsecase changeActiveStatusUsecase;

	@Operation(
			summary = "회원 상태 변경",
			description = "RequestBody의 activeStatus를 사용해 회원 상태를 AM,RM,CM,OB 중 하나로 변경한다.")
	@PutMapping("/activeStatus")
	public ApiResponse<SuccessBody<CommandMemberResponse>> changeActiveStatus(
			@Member Long memberId, @RequestBody @Valid ChangeActiveStatusRequest request) {
		CommandMemberResponse response = changeActiveStatusUsecase.changeStatus(memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Operation(
			summary = "관리자_회원 상태 변경",
			description = "RequestBody의 activeStatus를 사용해 회원 상태를 AM,RM,CM,OB 중 하나로 변경한다.")
	@PutMapping("/{memberId}")
	public ApiResponse<SuccessBody<CommandMemberResponse>> adminChangeActiveStatus(
			@Member Long adminMemberId,
			@PathVariable("memberId") Long memberId,
			@RequestBody @Valid ChangeActiveStatusRequest request) {
		CommandMemberResponse response =
				changeActiveStatusUsecase.adminChangeStatus(adminMemberId, memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Operation(summary = "관리자_회원 삭제", description = "Pathvariable의 memberId를 사용해 회원을 삭제합니다.")
	@DeleteMapping("/{memberId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long adminMemberId, @PathVariable("memberId") Long memberId) {
		changeActiveStatusUsecase.delete(adminMemberId, memberId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}
}
