package com.blackcompany.eeos.program.presentation.controller;

import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import com.blackcompany.eeos.program.application.dto.request.CalendarApplicationCommand;
import com.blackcompany.eeos.program.application.dto.response.CalendarPeriodApplicationQuery;
import com.blackcompany.eeos.program.application.usecase.GetCalendarUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateCalendarUsecase;
import com.blackcompany.eeos.program.presentation.dto.UpdateCalendarRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalendarController {
	private final GetCalendarUsecase getCalendarUsecase;
	private final UpdateCalendarUsecase updateCalendarUsecase;

	@PutMapping("/admin/calendars") // TODO : 인터셉터 단에서 어드민 검증
	public ApiResponse<ApiResponseBody.SuccessBody<CalendarPeriodApplicationQuery>> updateCalendar(
			@RequestBody @Valid UpdateCalendarRequest request) {
		CalendarPeriodApplicationQuery response =
				updateCalendarUsecase.updateCalendar(
						new CalendarApplicationCommand(request.startDate(), request.endDate()));
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@GetMapping("/calendars")
	public ApiResponse<ApiResponseBody.SuccessBody<CalendarPeriodApplicationQuery>> getCalendar() {
		CalendarPeriodApplicationQuery response = getCalendarUsecase.getCalendar();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
