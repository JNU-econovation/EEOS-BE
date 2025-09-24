package com.blackcompany.eeos.program.presentation.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

public record UpdateSemesterPeriodRequest(
		@NotNull(message = "시작일은 필수값입니다.") Timestamp startDate,
		@NotNull(message = "종료일은 필수값입니다.") Timestamp endDate) {

	@AssertTrue(message = "종료일은 시작일 이후여야 합니다.")
	public boolean isEndDateAfterStartDate() {
		return endDate.getTime() > startDate.getTime();
	}
}
