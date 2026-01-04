package com.blackcompany.eeos.notification.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.usecase.CreateMemberPushTokenUsecase;
import com.blackcompany.eeos.notification.application.usecase.DeleteAllMemberPushTokensUsecase;
import com.blackcompany.eeos.notification.application.usecase.DeleteMemberPushTokenUsecase;
import com.blackcompany.eeos.notification.presentation.docs.MemberPushTokenApi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pushToken")
public class MemberPushTokenController implements MemberPushTokenApi {

	private final CreateMemberPushTokenUsecase createMemberPushTokenUsecase;
	private final DeleteMemberPushTokenUsecase deleteMemberPushTokenUsecase;
	private final DeleteAllMemberPushTokensUsecase deleteAllMemberPushTokensUsecase;

	@Override
	@PostMapping
	public ApiResponse<SuccessBody<Void>> create(@Member Long memberId, @RequestBody @Valid CreateMemberPushTokenRequest request) {
		createMemberPushTokenUsecase.create(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.CREATE);
	}


	@Override
	@DeleteMapping
	public ApiResponse<SuccessBody<Void>> delete(@Member Long memberId, @RequestBody @Valid DeleteMemberPushTokenRequest request){
		deleteMemberPushTokenUsecase.delete(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@DeleteMapping("/all")
	public ApiResponse<SuccessBody<Void>> deleteAll(@Member Long memberId){
		deleteAllMemberPushTokensUsecase.deleteAllMemberPushTokens(memberId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}
}
