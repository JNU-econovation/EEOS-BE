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
public class QueryTargetInfoResponse implements AbstractResponseDto {
	private String name;
	private String status;
	private String content;
}
