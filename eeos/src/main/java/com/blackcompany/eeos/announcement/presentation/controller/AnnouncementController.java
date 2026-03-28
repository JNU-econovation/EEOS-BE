package com.blackcompany.eeos.announcement.presentation.controller;

import com.blackcompany.eeos.announcement.application.dto.GetAnnouncementsResponse;
import com.blackcompany.eeos.announcement.application.usecase.GetAnnouncementsUsecase;
import com.blackcompany.eeos.announcement.presentation.docs.AnnouncementApi;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController implements AnnouncementApi {

	private final GetAnnouncementsUsecase getAnnouncementsUsecase;

	@Override
	@GetMapping
	public ApiResponse<SuccessBody<GetAnnouncementsResponse>> getAnnouncements() {
		GetAnnouncementsResponse response = getAnnouncementsUsecase.getAnnouncements();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
