package com.blackcompany.eeos.attend.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangeAttendStatusResponse implements AbstractResponseDto {
	private String name;
	private String attendStatus;
}
