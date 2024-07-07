package com.blackcompany.eeos.comment.application.model.converter;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class CommentModelConverter
		implements AbstractDtoConverter<CreateCommentRequest, CommentModel> {

	@Override
	public CommentModel from(CreateCommentRequest request) {
		return CommentModel.builder()
				.programId(request.getProgramId())
				.superCommentId(request.getParentsCommentId())
				.content(request.getContent())
				.presentingTeam(request.getTeamId())
				.build();
	}

	public CommentModel from(Long memberId, CreateCommentRequest request) {
		return from(request).toBuilder().writer(memberId).build();
	}
}
