package com.blackcompany.eeos.comment.fixture;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.model.CommentType;
import com.blackcompany.eeos.comment.persistence.CommentEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CommentFixture {

	private static final Long DEFAULT_PROGRAM_ID = 1L;
	private static final Long DEFAULT_TEAM_ID = 1L;
	private static final Long SUPER_COMMENT_ID = -1L;
	private static final Timestamp NOW = Timestamp.valueOf(LocalDateTime.now());

	public static CommentModel 댓글_모델(Long commentId, Long writerId) {
		return CommentModel.builder()
				.id(commentId)
				.programId(DEFAULT_PROGRAM_ID)
				.presentingTeam(DEFAULT_TEAM_ID)
				.superCommentId(SUPER_COMMENT_ID)
				.content("테스트 댓글입니다.")
				.writer(writerId)
				.commentType(CommentType.NON_ANONYMOUS)
				.createdDate(NOW)
				.updatedDate(NOW)
				.build();
	}

	public static CommentModel 대댓글_모델(Long commentId, Long writerId, Long superCommentId) {
		return CommentModel.builder()
				.id(commentId)
				.programId(DEFAULT_PROGRAM_ID)
				.presentingTeam(DEFAULT_TEAM_ID)
				.superCommentId(superCommentId)
				.content("테스트 대댓글입니다.")
				.writer(writerId)
				.commentType(CommentType.NON_ANONYMOUS)
				.createdDate(NOW)
				.updatedDate(NOW)
				.build();
	}

	public static CommentModel 익명_댓글_모델(Long commentId, Long writerId) {
		return CommentModel.builder()
				.id(commentId)
				.programId(DEFAULT_PROGRAM_ID)
				.presentingTeam(DEFAULT_TEAM_ID)
				.superCommentId(SUPER_COMMENT_ID)
				.content("익명 테스트 댓글입니다.")
				.writer(writerId)
				.commentType(CommentType.ANONYMOUS)
				.createdDate(NOW)
				.updatedDate(NOW)
				.build();
	}

	public static CreateCommentRequest 댓글_생성_요청() {
		return CreateCommentRequest.builder()
				.programId(DEFAULT_PROGRAM_ID)
				.teamId(DEFAULT_TEAM_ID)
				.parentsCommentId(SUPER_COMMENT_ID)
				.content("테스트 댓글입니다.")
				.commentType(CommentType.NON_ANONYMOUS)
				.build();
	}

	public static CreateCommentRequest 대댓글_생성_요청(Long parentId) {
		return CreateCommentRequest.builder()
				.programId(DEFAULT_PROGRAM_ID)
				.teamId(DEFAULT_TEAM_ID)
				.parentsCommentId(parentId)
				.content("테스트 대댓글입니다.")
				.commentType(CommentType.NON_ANONYMOUS)
				.build();
	}

	public static UpdateCommentRequest 댓글_수정_요청() {
		return UpdateCommentRequest.builder().contents("수정된 댓글입니다.").build();
	}

	public static CommentEntity 댓글_엔티티(Long commentId, Long writerId) {
		return CommentEntity.builder()
				.id(commentId)
				.programId(DEFAULT_PROGRAM_ID)
				.presentingTeam(DEFAULT_TEAM_ID)
				.superCommentId(SUPER_COMMENT_ID)
				.content("테스트 댓글입니다.")
				.writer(writerId)
				.commentType(CommentType.NON_ANONYMOUS)
				.build();
	}
}
