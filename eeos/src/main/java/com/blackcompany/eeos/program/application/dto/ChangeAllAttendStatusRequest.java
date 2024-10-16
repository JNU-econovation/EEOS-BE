package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangeAllAttendStatusRequest implements AbstractRequestDto {
	private Long memberId;
	private String afterAttendStatus;
}
