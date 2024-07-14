package com.blackcompany.eeos.comment.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class CreateCommentRequest implements AbstractRequestDto {

	private @NotNull Long programId;
	private @NotNull Long teamId;
	private @NotNull Long parentsCommentId;
	private @NotNull String content;
}
