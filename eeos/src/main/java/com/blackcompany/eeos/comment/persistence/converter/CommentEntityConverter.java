package com.blackcompany.eeos.comment.persistence.converter;

import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.persistence.CommentEntity;
import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class CommentEntityConverter
		implements AbstractEntityConverter<CommentEntity, CommentModel> {

	@Override
	public CommentEntity toEntity(CommentModel model) {
		return CommentEntity.builder()
				.programId(model.getProgramId())
				.superCommentId(model.getSuperCommentId())
				.writer(model.getWriter())
				.presentingTeamId(model.getPresentingTeam())
				.content(model.getContent())
				.build();
	}

	@Override
	public CommentModel from(CommentEntity entity) {
		return CommentModel.builder()
				.id(entity.getId())
				.programId(entity.getProgramId())
				.presentingTeam(entity.getPresentingTeamId())
				.writer(entity.getWriter())
				.createdDate(entity.getCreatedDate())
				.updatedDate(entity.getUpdatedDate())
				.content(entity.getContent())
				.superCommentId(entity.getSuperCommentId())
				.build();
	}
}
