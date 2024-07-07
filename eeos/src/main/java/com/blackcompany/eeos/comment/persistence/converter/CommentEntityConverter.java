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
		CommentEntity entity =
				CommentEntity.builder()
						.superCommentId(model.getSuperCommentId())
						.writer(model.getWriter())
						.programId(model.getProgramId())
						.content(model.getContent())
						.presentingTeamId(model.getPresentingTeam())
						.build();
		return entity;
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
