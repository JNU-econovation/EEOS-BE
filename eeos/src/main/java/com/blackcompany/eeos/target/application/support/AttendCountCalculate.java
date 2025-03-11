package com.blackcompany.eeos.target.application.support;

import com.blackcompany.eeos.target.application.model.AttendModel;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AttendCountCalculate {

	public Long countByStatus(String status, List<AttendModel> attendModels) {
		return attendModels.stream().filter(attend -> attend.getStatus().equals(status)).count();
	}

	public Long penaltyPoint(List<AttendModel> attendModels) {
		return attendModels.stream().reduce(0L, (i, attend) -> i + attend.getPenaltyScore(), Long::sum);
	}
}
