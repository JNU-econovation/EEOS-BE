package com.blackcompany.eeos.calendar.application.dto;

import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import com.blackcompany.eeos.common.utils.DateConverter;
import java.util.Locale;

public record CalendarResponse(
		Long calendarId, String title, String url, String type, Long startAt, Long endAt, String writer)
		implements AbstractResponseDto {

	public static CalendarResponse toResponse(CalendarModel model, String writerName) {
		Long startAt = DateConverter.toMillis(model.getStartAt());
		Long endAt = DateConverter.toMillis(model.getEndAt());
		String type = model.getType().name().toLowerCase(Locale.ROOT);

		return new CalendarResponse(
				model.getId(), model.getTitle(), model.getUrl(), type, startAt, endAt, writerName);
	}
}
