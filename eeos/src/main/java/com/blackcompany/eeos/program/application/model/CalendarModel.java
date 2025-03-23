package com.blackcompany.eeos.program.application.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Slf4j
public class CalendarModel {
	private Timestamp startDate;
	private Timestamp endDate;

	public CalendarModel() {
		LocalDate now = LocalDate.now();

		// 9월 이후이면 9월 1일부터 다음 해 2월 말까지
		if (now.getMonthValue() >= Month.SEPTEMBER.getValue()) {
			startDate = Timestamp.valueOf(LocalDate.of(now.getYear(), Month.SEPTEMBER, 1).atStartOfDay());
			endDate =
					Timestamp.valueOf(
							LocalDate.of(
											now.getYear() + 1,
											Month.FEBRUARY,
											Month.FEBRUARY.length(Year.isLeap(now.getYear() + 1)))
									.atTime(23, 59, 59));
		} else {
			// 3월 이후이면 3월 1일부터 8월 말까지
			startDate = Timestamp.valueOf(LocalDate.of(now.getYear(), Month.MARCH, 1).atStartOfDay());
			endDate = Timestamp.valueOf(LocalDate.of(now.getYear(), Month.AUGUST, 31).atTime(23, 59, 59));
		}
	}
}
