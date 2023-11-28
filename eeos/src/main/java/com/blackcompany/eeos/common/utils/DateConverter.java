package com.blackcompany.eeos.common.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateConverter {
	private static final String KOREA_ZONE_OFFSET = "+09:00";

	public static Timestamp toEpochSecond(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}

		return Timestamp.valueOf(localDate.atStartOfDay());
	}

	public static LocalDate toLocalDate(Long epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		return Instant.ofEpochSecond(epochSecond).atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
	}

	public static Timestamp toEpochSecond(Timestamp epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		LocalDate localDate = toLocalDate(epochSecond);
		return toEpochSecond(localDate);
	}

	public static LocalDate toLocalDate(Timestamp epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		return epochSecond.toLocalDateTime().toLocalDate();
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
