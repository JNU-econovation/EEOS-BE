package com.blackcompany.eeos.comment.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class QueryCommentResponse implements AbstractResponseDto {

	private Long commentId;
	private Long teamId;
	private String writer;
	private String accessRight;
	private String time;
	private String content;
	private List<QueryAnswerResponse> answers;
}
