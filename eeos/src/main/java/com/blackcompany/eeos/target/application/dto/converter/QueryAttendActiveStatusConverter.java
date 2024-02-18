package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.target.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QueryAttendActiveStatusConverter {
	public QueryAttendActiveStatusResponse of(List<AttendInfoActiveStatusResponse> response) {
		return QueryAttendActiveStatusResponse.builder().members(response).build();
	}
}
