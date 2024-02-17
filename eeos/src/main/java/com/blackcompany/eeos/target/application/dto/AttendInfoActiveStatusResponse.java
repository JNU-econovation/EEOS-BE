package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AttendInfoActiveStatusResponse implements AbstractResponseDto {
	private Long memberId;
	private String name;
	private String activeStatus;
	private String attendStatus;
}
