package com.blackcompany.eeos.program.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GetProgramResponse implements AbstractProgramResponse {

	private Long id;
	private String title;
	private Long programDate;
	private String content;
	private String eventStatus;
}
