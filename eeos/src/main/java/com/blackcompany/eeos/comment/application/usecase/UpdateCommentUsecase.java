package com.blackcompany.eeos.comment.application.usecase;

import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.model.CommentModel;

public interface UpdateCommentUsecase {

	CommentModel update(Long memberId, Long commentId, UpdateCommentRequest request);
}
