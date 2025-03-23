package com.blackcompany.eeos.program.application.dto.request;

import com.blackcompany.eeos.common.support.dto.AbstractApplicationDto;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

public record CalendarApplicationCommand(
		@NotNull(message = "시작 날짜는 필수입니다") Timestamp startDate,
		@NotNull(message = "종료 날짜는 필수입니다") Timestamp endDate)
		implements AbstractApplicationDto {}
