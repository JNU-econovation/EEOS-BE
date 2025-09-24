package com.blackcompany.eeos.calendar.presentation.controller;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;
import com.blackcompany.eeos.calendar.application.dto.CalendarQuery;
import com.blackcompany.eeos.calendar.application.dto.CalendarResponse;
import com.blackcompany.eeos.calendar.application.dto.CalendarResponses;
import com.blackcompany.eeos.calendar.application.dto.CalendarUpdateCommand;
import com.blackcompany.eeos.calendar.application.usecase.CreateCalendarUsecase;
import com.blackcompany.eeos.calendar.application.usecase.DeleteCalendarUsecase;
import com.blackcompany.eeos.calendar.application.usecase.GetCalendarUsecase;
import com.blackcompany.eeos.calendar.application.usecase.UpdateCalendarUsecase;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/calendars")
@RestController
@RequiredArgsConstructor
public class CalendarController {

	private final CreateCalendarUsecase createUsecase;
	private final GetCalendarUsecase getUsecase;
	private final UpdateCalendarUsecase updateUsecase;
	private final DeleteCalendarUsecase deleteUsecase;

	@PostMapping
	public ApiResponse<SuccessBody<Long>> create(@RequestBody CalendarCreateCommand request) {
		Long createdId = createUsecase.create(request);
		return ApiResponseGenerator.success(createdId, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@GetMapping
	public ApiResponse<SuccessBody<CalendarResponses>> getCalendar(
			@RequestParam("year") Integer year,
			@RequestParam("month") Integer month,
			@RequestParam(value = "date", required = false) Integer date,
			@RequestParam(value = "duration", required = false) Integer duration) {
		List<CalendarResponse> calendars =
				getUsecase.getCalendar(new CalendarQuery(year, month, date, duration));
		CalendarResponses responses = new CalendarResponses(calendars);
		return ApiResponseGenerator.success(responses, HttpStatus.OK, MessageCode.GET);
	}

	@PutMapping("/{calendarId}")
	public ApiResponse<SuccessBody<Long>> updateCalendar(
			@PathVariable("calendarId") Long calendarId, @RequestBody CalendarUpdateCommand command) {
		Long id = updateUsecase.update(calendarId, command);
		return ApiResponseGenerator.success(id, HttpStatus.OK, MessageCode.UPDATE);
	}

	@DeleteMapping("/{calendarId}")
	public ApiResponse<SuccessBody<Void>> deleteCalendar(
			@PathVariable("calendarId") Long calendarId) {
		deleteUsecase.delete(calendarId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@GetMapping("/d-day")
	public ApiResponse<SuccessBody<CalendarResponses>> getCalendarsForDDay(
			@RequestParam("measure") @PositiveOrZero int measure) {
		List<CalendarResponse> calendars = getUsecase.getCalendarForDDay(measure);
		CalendarResponses responses = new CalendarResponses(calendars);
		return ApiResponseGenerator.success(responses, HttpStatus.OK, MessageCode.GET);
	}
}
