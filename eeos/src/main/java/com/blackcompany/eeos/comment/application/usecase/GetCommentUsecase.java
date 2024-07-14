package com.blackcompany.eeos.comment.application.usecase;

import com.blackcompany.eeos.comment.application.model.CommentModel;
import java.util.List;

public interface GetCommentUsecase {

	List<CommentModel> getComments(Long memberId, Long programId, Long teamId);

	List<CommentModel> getAnswerComments(Long commentId);
}
