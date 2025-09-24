package com.blackcompany.eeos.program.application.dto.response;

import com.blackcompany.eeos.common.support.dto.AbstractApplicationDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SemesterPeriodApplicationQuery implements AbstractApplicationDto {
	private Timestamp startDate;
	private Timestamp endDate;
}
