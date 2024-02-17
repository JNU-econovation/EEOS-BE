package com.blackcompany.eeos.target.application.model;

import com.blackcompany.eeos.target.application.exception.NotFoundAttendStatusException;
import java.util.Arrays;

public enum AttendStatus {

	/** 관련있음, 참석 */
	ATTEND("attend"),
	/** 관련있음, 불참 */
	ABSENT("absent"),
	/** 관련있음, 지각 */
	LATE("late"),
	/** 관련있음, 미응답 */
	NONRESPONSE("nonResponse"),
	/** 관련없음 */
	NONRELATED("nonRelated");
	private final String status;

	AttendStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static AttendStatus find(String status) {
		return Arrays.stream(AttendStatus.values())
				.filter(attendStatus -> attendStatus.getStatus().equals(status))
				.findAny()
				.orElseThrow(() -> new NotFoundAttendStatusException(status));
	}

	public static boolean isSame(String source, AttendStatus target) {
		return target.getStatus().equals(source);
	}
}
