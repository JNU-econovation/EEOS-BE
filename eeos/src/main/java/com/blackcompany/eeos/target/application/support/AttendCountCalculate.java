package com.blackcompany.eeos.target.application.support;

import com.blackcompany.eeos.target.application.model.AttendModel;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AttendCountCalculate {

	public Long attendCount(List<AttendModel> attendModels) {
		return attendModels.stream()
				.filter(attend -> AttendStatus.find(attend.getStatus()).equals(AttendStatus.ATTEND))
				.count();
	}

	public Long absentCount(List<AttendModel> attendModels) {
		return attendModels.stream()
				.filter(attend -> AttendStatus.find(attend.getStatus()).equals(AttendStatus.ABSENT))
				.count();
	}

	public Long lateCount(List<AttendModel> attendModels) {
		return attendModels.stream()
				.filter(attend -> AttendStatus.find(attend.getStatus()).equals(AttendStatus.LATE))
				.count();
	}

	public Long penaltyPoint(List<AttendModel> attendModels) {
		return attendModels.stream().reduce(0L, (i, attend) -> i + attend.getPenaltyScore(), Long::sum);
	}
}
