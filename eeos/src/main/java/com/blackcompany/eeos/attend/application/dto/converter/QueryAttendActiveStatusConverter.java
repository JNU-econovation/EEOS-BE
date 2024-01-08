package com.blackcompany.eeos.attend.application.dto.converter;

import com.blackcompany.eeos.attend.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendActiveStatusResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QueryAttendActiveStatusConverter {
	public QueryAttendActiveStatusResponse of(List<AttendInfoActiveStatusResponse> response) {
		return QueryAttendActiveStatusResponse.builder().members(response).build();
	}
}
