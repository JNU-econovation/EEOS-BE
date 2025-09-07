package com.blackcompany.eeos.common.utils;

import java.time.YearMonth;
import lombok.experimental.UtilityClass;

/** DateUtil => 날짜의 유틸 기능과 관련된 클래스 ex) 한 날짜의 마지막 날 구하기, 윤년인지 아닌지 판별하기 */
@UtilityClass
public class DateUtil {

	/**
	 * 년, 월을 받아 해당 년도의 해당 월이 며칠까지 존재하는지 반환
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDay(int year, int month) {
		return YearMonth.of(year, month).lengthOfMonth();
	}
}
