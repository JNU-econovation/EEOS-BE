package com.blackcompany.eeos.member.presentation.controller;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.member.application.dto.QueryMemberResponse;
import com.blackcompany.eeos.member.application.usecase.GetMembersByActiveStatus;
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
	private GetMembersByActiveStatus getMembersByActiveStatus;

	@GetMapping("/members")
	public ApiResponse<SuccessBody<QueryMemberResponse>> findAll(
			@RequestParam("activeStatus") String activeStatus) {

		QueryMemberResponse response = getMembersByActiveStatus.execute(activeStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
