package com.blackcompany.eeos.program.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

public record UpdateCalendarRequest(
		@NotNull(message = "시작일은 필수값입니다.") Timestamp startDate,
		@NotNull(message = "종료일은 필수값입니다.") Timestamp endDate) {}
