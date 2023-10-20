package com.blackcompany.eeos.attend.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangeStatusRequest implements AbstractRequestDto {
	private Long memberId;
	private String beforeAttendStatus;
	private String afterAttendStatus;
}
