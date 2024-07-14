package com.blackcompany.eeos.comment.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/** 조회 결과의 응답 객체 */
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class CommandCommentResponse implements AbstractResponseDto {

	private @NotNull Long commentId;
}
