package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QueryProgramsResponse implements AbstractResponseDto {

	private Long programId;
	private String title;
	private Timestamp programDate;
	private String content;
}
