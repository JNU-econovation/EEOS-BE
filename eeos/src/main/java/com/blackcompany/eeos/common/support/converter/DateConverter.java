package com.blackcompany.eeos.common.support.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateConverter {
	private static final String KOREA_ZONE_OFFSET = "+09:00";

	public static Long toEpochSecond(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}

		return localDateTime.toEpochSecond(ZoneOffset.of(KOREA_ZONE_OFFSET));
	}

	public static Long toEpochSecond(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}

		return localDate.toEpochSecond(LocalTime.parse("00:00:00"), ZoneOffset.of(KOREA_ZONE_OFFSET));
	}

	public static LocalDate toLocalDate(Long epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		return Instant.ofEpochSecond(epochSecond)
				.atZone(ZoneOffset.of(KOREA_ZONE_OFFSET))
				.toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Long epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		return Instant.ofEpochSecond(epochSecond)
				.atZone(ZoneOffset.of(KOREA_ZONE_OFFSET))
				.toLocalDateTime();
	}
}
