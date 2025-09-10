package com.blackcompany.eeos.calendar.application.model;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CalendarModel {

	private Long id;
	private String title;
	private Long writer;
	private LocalDateTime startAt;
	private LocalDateTime endAt;
	private CalendarType type;
	private String url;

	public boolean isWriter(Long updater) {
		return this.writer.equals(updater);
	}

	public CalendarModel updateTitle(String title) {
		this.title = title;
		return this;
	}

	public CalendarModel updateStartAt(LocalDateTime startAt) {
		this.startAt = startAt;
		return this;
	}

	public CalendarModel updateEndAt(LocalDateTime endAt) {
		this.endAt = endAt;
		return this;
	}

	public CalendarModel updateType(String type) {
		this.type = CalendarType.findByName(type);
		return this;
	}

	public CalendarModel updateUrl(String url) {
		this.url = url;
		return this;
	}

	public static CalendarModel create(
			String title,
			LocalDateTime startAt,
			LocalDateTime endAt,
			String type,
			String url,
			Long writer) {
		return CalendarModel.builder()
				.title(title)
				.writer(writer)
				.startAt(startAt)
				.endAt(endAt)
				.url(url)
				.type(CalendarType.findByName(type))
				.build();
	}

	public static CalendarModel load(
			Long id,
			String title,
			LocalDateTime startAt,
			LocalDateTime endAt,
			CalendarType type,
			String url,
			Long writer) {
		return CalendarModel.builder()
				.id(id)
				.title(title)
				.writer(writer)
				.startAt(startAt)
				.endAt(endAt)
				.url(url)
				.type(type)
				.build();
	}
}
