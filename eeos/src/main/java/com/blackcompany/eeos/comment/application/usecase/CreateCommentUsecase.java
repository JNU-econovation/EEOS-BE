package com.blackcompany.eeos.comment.application.usecase;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.model.CommentModel;

public interface CreateCommentUsecase {

	/**
	 * 질문 및 코멘트를 저장한다.
	 *
	 * @param request 프로그램 id
	 * @param request 코멘트 작성자 id
	 * @param request 코멘트 저장을 위한 request 객체
	 * @return comment id 전달
	 */
	CommentModel create(Long memberId, CreateCommentRequest request);
}
