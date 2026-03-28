package com.blackcompany.eeos.common.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.experimental.UtilityClass;

// DateConverter => 날짜 변환과 관련된 유틸 클래스
@UtilityClass
public class DateConverter {
	private static final String KST = "Asia/Seoul";

	public static Timestamp toEpochSecond(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}

		return Timestamp.valueOf(localDate.atStartOfDay());
	}

	public static Timestamp toEpochSecond(Timestamp epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		LocalDate localDate = toLocalDate(epochSecond);
		return toEpochSecond(localDate);
	}

	private static LocalDate toLocalDate(Timestamp epochSecond) {
		if (epochSecond == null) {
			return null;
		}

		return epochSecond.toLocalDateTime().toLocalDate();
	}

	public static LocalDate toLocalDate(Long epochMilli) {
		if (epochMilli == null) {
			return null;
		}

		return Instant.ofEpochSecond(epochMilli / 1000).atZone(ZoneId.of(KST)).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Long epochMilli) {
		if (epochMilli == null) {
			return null;
		}

		return Instant.ofEpochSecond(epochMilli / 1000).atZone(ZoneId.of(KST)).toLocalDateTime();
	}

	public static Long toMillis(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.of(KST)).toInstant().toEpochMilli();
	}

	public static Long toMillis(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return localDate.atStartOfDay(ZoneId.of(KST)).toInstant().toEpochMilli();
	}
}
