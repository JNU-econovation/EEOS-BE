package com.blackcompany.eeos.attend.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QueryAttendStatusResponse implements AbstractResponseDto {
	private List<AttendInfoResponse> members;
}
