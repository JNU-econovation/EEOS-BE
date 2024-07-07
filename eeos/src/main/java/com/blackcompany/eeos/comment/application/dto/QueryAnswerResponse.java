package com.blackcompany.eeos.comment.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QueryAnswerResponse implements AbstractResponseDto {

	private Long commentId;
	private String writer;
	private String accessRight;
	private String time;
	private String content;
}
