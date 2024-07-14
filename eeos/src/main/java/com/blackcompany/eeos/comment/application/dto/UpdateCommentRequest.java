package com.blackcompany.eeos.comment.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class UpdateCommentRequest implements AbstractRequestDto {

	private @NotNull String content;
}
