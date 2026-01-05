package com.blackcompany.eeos.comment.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.exception.DeniedCommentEditException;
import com.blackcompany.eeos.comment.application.exception.NotCreateAdminCommentException;
import com.blackcompany.eeos.comment.application.exception.NotFoundCommentException;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.model.converter.CommentModelConverter;
import com.blackcompany.eeos.comment.fixture.CommentFixture;
import com.blackcompany.eeos.comment.persistence.CommentEntity;
import com.blackcompany.eeos.comment.persistence.CommentRepository;
import com.blackcompany.eeos.comment.persistence.converter.CommentEntityConverter;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@Mock CommentRepository commentRepository;
	@Mock CommentModelConverter commentModelConverter;
	@Mock CommentEntityConverter commentEntityConverter;
	@Mock ProgramRepository programRepository;
	@Mock TeamRepository teamRepository;
	@Mock QueryMemberService memberService;
	@InjectMocks CommentService commentService;

	private static final Long MEMBER_ID = 1L;
	private static final Long OTHER_MEMBER_ID = 2L;
	private static final Long COMMENT_ID = 1L;
	private static final Long PROGRAM_ID = 1L;
	private static final Long TEAM_ID = 1L;

	@Test
	@DisplayName("댓글을 생성할 수 있다")
	void create_comment_success() {
		// given
		CreateCommentRequest request = CommentFixture.댓글_생성_요청();
		CommentModel model = CommentFixture.댓글_모델(COMMENT_ID, MEMBER_ID);
		CommentEntity entity = CommentFixture.댓글_엔티티(COMMENT_ID, MEMBER_ID);
		MemberModel member = MemberModel.builder().id(MEMBER_ID).isAdmin(false).build();

		when(memberService.findMember(MEMBER_ID)).thenReturn(member);
		when(commentModelConverter.from(MEMBER_ID, request)).thenReturn(model);
		when(teamRepository.existsById(TEAM_ID)).thenReturn(true);
		when(programRepository.existsById(PROGRAM_ID)).thenReturn(true);
		when(commentEntityConverter.toEntity(any(CommentModel.class))).thenReturn(entity);
		when(commentRepository.save(any(CommentEntity.class))).thenReturn(entity);
		when(commentEntityConverter.from(any(CommentEntity.class))).thenReturn(model);

		// when
		CommentModel result = commentService.create(MEMBER_ID, request);

		// then
		assertNotNull(result);
		assertEquals(COMMENT_ID, result.getId());
		verify(commentRepository).save(any(CommentEntity.class));
	}

	@Test
	@DisplayName("어드민은 댓글을 생성할 수 없다")
	void create_comment_admin_denied() {
		// given
		CreateCommentRequest request = CommentFixture.댓글_생성_요청();
		MemberModel adminMember = MemberModel.builder().id(MEMBER_ID).isAdmin(true).build();

		when(memberService.findMember(MEMBER_ID)).thenReturn(adminMember);

		// when & then
		assertThrows(
				NotCreateAdminCommentException.class, () -> commentService.create(MEMBER_ID, request));
	}

	@Test
	@DisplayName("댓글을 수정할 수 있다")
	void update_comment_success() {
		// given
		UpdateCommentRequest request = CommentFixture.댓글_수정_요청();
		CommentModel existingComment = CommentFixture.댓글_모델(COMMENT_ID, MEMBER_ID);
		CommentEntity entity = CommentFixture.댓글_엔티티(COMMENT_ID, MEMBER_ID);

		when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(entity));
		when(commentEntityConverter.from(any(CommentEntity.class))).thenReturn(existingComment);
		when(commentRepository.updateById(COMMENT_ID, request.getContents())).thenReturn(1);

		// when
		CommentModel result = commentService.update(MEMBER_ID, COMMENT_ID, request);

		// then
		assertNotNull(result);
		verify(commentRepository).updateById(COMMENT_ID, request.getContents());
	}

	@Test
	@DisplayName("작성자가 아니면 댓글을 수정할 수 없다")
	void update_comment_denied_not_writer() {
		// given
		UpdateCommentRequest request = CommentFixture.댓글_수정_요청();
		CommentModel existingComment = CommentFixture.댓글_모델(COMMENT_ID, MEMBER_ID);
		CommentEntity entity = CommentFixture.댓글_엔티티(COMMENT_ID, MEMBER_ID);

		when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(entity));
		when(commentEntityConverter.from(any(CommentEntity.class))).thenReturn(existingComment);

		// when & then
		assertThrows(
				DeniedCommentEditException.class,
				() -> commentService.update(OTHER_MEMBER_ID, COMMENT_ID, request));
	}

	@Test
	@DisplayName("댓글을 삭제할 수 있다")
	void delete_comment_success() {
		// given
		CommentModel existingComment = CommentFixture.댓글_모델(COMMENT_ID, MEMBER_ID);
		CommentEntity entity = CommentFixture.댓글_엔티티(COMMENT_ID, MEMBER_ID);

		when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(entity));
		when(commentEntityConverter.from(any(CommentEntity.class))).thenReturn(existingComment);
		doNothing().when(commentRepository).deleteById(COMMENT_ID);

		// when
		commentService.delete(MEMBER_ID, COMMENT_ID);

		// then
		verify(commentRepository).deleteById(COMMENT_ID);
	}

	@Test
	@DisplayName("작성자가 아니면 댓글을 삭제할 수 없다")
	void delete_comment_denied_not_writer() {
		// given
		CommentModel existingComment = CommentFixture.댓글_모델(COMMENT_ID, MEMBER_ID);
		CommentEntity entity = CommentFixture.댓글_엔티티(COMMENT_ID, MEMBER_ID);

		when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(entity));
		when(commentEntityConverter.from(any(CommentEntity.class))).thenReturn(existingComment);

		// when & then
		assertThrows(
				DeniedCommentEditException.class, () -> commentService.delete(OTHER_MEMBER_ID, COMMENT_ID));
	}

	@Test
	@DisplayName("존재하지 않는 댓글은 조회할 수 없다")
	void find_comment_not_found() {
		// given
		UpdateCommentRequest request = CommentFixture.댓글_수정_요청();

		when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

		// when & then
		assertThrows(
				NotFoundCommentException.class,
				() -> commentService.update(MEMBER_ID, COMMENT_ID, request));
	}

	@Test
	@DisplayName("프로그램과 팀으로 댓글 목록을 조회할 수 있다")
	void get_comments_by_program_and_team() {
		// given
		CommentModel comment1 = CommentFixture.댓글_모델(1L, MEMBER_ID);
		CommentModel comment2 = CommentFixture.댓글_모델(2L, OTHER_MEMBER_ID);
		CommentEntity entity1 = CommentFixture.댓글_엔티티(1L, MEMBER_ID);
		CommentEntity entity2 = CommentFixture.댓글_엔티티(2L, OTHER_MEMBER_ID);

		when(commentRepository.findCommentByProgramIdAndPresentingTeamId(PROGRAM_ID, TEAM_ID))
				.thenReturn(List.of(entity1, entity2));
		when(commentEntityConverter.from(entity1)).thenReturn(comment1);
		when(commentEntityConverter.from(entity2)).thenReturn(comment2);

		// when
		List<CommentModel> result = commentService.getComments(MEMBER_ID, PROGRAM_ID, TEAM_ID);

		// then
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	@DisplayName("teamId가 null이면 예외가 발생한다")
	void get_comments_team_id_null() {
		// when & then
		assertThrows(
				NullPointerException.class, () -> commentService.getComments(MEMBER_ID, PROGRAM_ID, null));
	}

	@Test
	@DisplayName("programId가 null이면 예외가 발생한다")
	void get_comments_program_id_null() {
		// when & then
		assertThrows(
				NullPointerException.class, () -> commentService.getComments(MEMBER_ID, null, TEAM_ID));
	}

	@Test
	@DisplayName("대댓글 목록을 조회할 수 있다")
	void get_answer_comments() {
		// given
		CommentModel reply1 = CommentFixture.대댓글_모델(2L, MEMBER_ID, COMMENT_ID);
		CommentModel reply2 = CommentFixture.대댓글_모델(3L, OTHER_MEMBER_ID, COMMENT_ID);
		CommentEntity entity1 = CommentFixture.댓글_엔티티(2L, MEMBER_ID);
		CommentEntity entity2 = CommentFixture.댓글_엔티티(3L, OTHER_MEMBER_ID);

		when(commentRepository.findCommentBySuperCommentId(COMMENT_ID))
				.thenReturn(List.of(entity1, entity2));
		when(commentEntityConverter.from(entity1)).thenReturn(reply1);
		when(commentEntityConverter.from(entity2)).thenReturn(reply2);

		// when
		List<CommentModel> result = commentService.getAnswerComments(COMMENT_ID);

		// then
		assertNotNull(result);
		assertEquals(2, result.size());
	}
}
