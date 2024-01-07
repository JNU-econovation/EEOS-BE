package com.blackcompany.eeos.attend.application.dto.converter;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QueryAttendStatusResponseConverter {
	public QueryAttendStatusResponse of(List<AttendInfoResponse> response) {
		return QueryAttendStatusResponse.builder().members(response).build();
	}
}
