package com.blackcompany.eeos.calendar.presentation.controller;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;
import com.blackcompany.eeos.calendar.application.usecase.CalendarCreateUsecase;
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

@RequestMapping("/api/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarCreateUsecase createUsecase;

    @PostMapping
    public ApiResponse<SuccessBody<Long>> create(@RequestBody  CalendarCreateCommand request){
        Long createdId = createUsecase.create(request);
        return ApiResponseGenerator.success(createdId, HttpStatus.CREATED, MessageCode.CREATE);
    }

}
