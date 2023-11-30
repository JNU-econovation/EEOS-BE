package com.blackcompany.eeos.common.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.experimental.UtilityClass;

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
}
