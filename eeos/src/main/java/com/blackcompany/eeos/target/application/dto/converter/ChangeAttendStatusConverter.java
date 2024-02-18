package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class ChangeAttendStatusConverter {
	public ChangeAttendStatusResponse from(String name, String attendStatus) {
		return ChangeAttendStatusResponse.builder().name(name).attendStatus(attendStatus).build();
	}
}
