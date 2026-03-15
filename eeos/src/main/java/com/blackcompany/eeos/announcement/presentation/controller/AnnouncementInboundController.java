package com.blackcompany.eeos.announcement.presentation.controller;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.announcement.application.usecase.SaveAnnouncementUsecase;
import com.blackcompany.eeos.announcement.presentation.docs.AnnouncementInboundApi;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class AnnouncementInboundController implements AnnouncementInboundApi {

	private final SaveAnnouncementUsecase saveAnnouncementUsecase;

	@Override
	@PostMapping("/announcements")
	public ApiResponse<SuccessBody<Void>> save(@RequestBody SaveAnnouncementRequest request) {
		saveAnnouncementUsecase.save(request);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.CREATE);
	}
}
