package com.blackcompany.eeos.program.application.domain;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.common.support.converter.DateConverter;
import com.blackcompany.eeos.program.application.model.EventStatus;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramModel implements AbstractModel {
	private Long id;
	private String title;

	private String content;

	private Long userId;

	private Timestamp programDate;
	private String eventStatus;

	public EventStatus calculateEventStatus() {
		LocalDateTime nowDate = DateConverter.toLocalDateTime(Instant.now().getEpochSecond());
		LocalDateTime programDate = DateConverter.toLocalDateTime(this.programDate.getTime());

		if (programDate.isBefore(nowDate)) {
			return EventStatus.END;
		}
		return EventStatus.ING;
	}
}
