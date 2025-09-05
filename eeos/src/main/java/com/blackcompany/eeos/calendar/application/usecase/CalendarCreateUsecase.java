package com.blackcompany.eeos.calendar.application.usecase;

import java.time.LocalDateTime;

public interface CalendarCreateUsecase {

    Long create(String title, String content, String url, String type, LocalDateTime startAt, LocalDateTime endAt);

}
