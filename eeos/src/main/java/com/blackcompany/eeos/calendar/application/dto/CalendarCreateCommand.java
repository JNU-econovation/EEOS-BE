package com.blackcompany.eeos.calendar.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;

public record CalendarCreateCommand(String title, String url, String type, Long startAt, Long endAt)
		implements AbstractRequestDto {}
