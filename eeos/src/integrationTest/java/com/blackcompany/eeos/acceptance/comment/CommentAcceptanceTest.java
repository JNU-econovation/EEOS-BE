package com.blackcompany.eeos.acceptance.comment;

import static com.blackcompany.eeos.acceptance.comment.CommentSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blackcompany.eeos.acceptance.common.AcceptanceTest;
import com.blackcompany.eeos.acceptance.common.TokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("댓글 인수 테스트")
class CommentAcceptanceTest extends AcceptanceTest {

	private String userToken;
	private String anotherUserToken;
	private static final Long PROGRAM_ID = 1L;
	private static final Long TEAM_ID = 1L;
	private static final Long SUPER_COMMENT_ID = -1L;

	@BeforeEach
	void setUpToken() {
		userToken = TokenProvider.createUserToken(1L);
		anotherUserToken = TokenProvider.createUserToken(2L);
	}

	@Test
	@DisplayName("댓글을 생성할 수 있다")
	void createComment() {
		// when
		ExtractableResponse<Response> response =
				댓글_생성_요청(
						userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "테스트 댓글입니다", "NON_ANONYMOUS");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getLong("data.commentId")).isNotNull();
	}

	@Test
	@DisplayName("익명 댓글을 생성할 수 있다")
	void createAnonymousComment() {
		// when
		ExtractableResponse<Response> response =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "익명 테스트 댓글입니다", "ANONYMOUS");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("댓글 목록을 조회할 수 있다")
	void getComments() {
		// given
		댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "조회 테스트 댓글", "NON_ANONYMOUS");

		// when
		ExtractableResponse<Response> response = 댓글_목록_조회_요청(userToken, PROGRAM_ID, TEAM_ID);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("작성자가 댓글을 수정할 수 있다")
	void updateComment() {
		// given
		ExtractableResponse<Response> createResponse =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "수정 전 댓글", "NON_ANONYMOUS");
		Long commentId = createResponse.jsonPath().getLong("data.commentId");

		// when
		ExtractableResponse<Response> response = 댓글_수정_요청(userToken, commentId, "수정 후 댓글");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("작성자가 아니면 댓글을 수정할 수 없다")
	void updateComment_denied_for_non_writer() {
		// given
		ExtractableResponse<Response> createResponse =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "다른 사람 댓글", "NON_ANONYMOUS");
		Long commentId = createResponse.jsonPath().getLong("data.commentId");

		// when
		ExtractableResponse<Response> response = 댓글_수정_요청(anotherUserToken, commentId, "수정 시도");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("작성자가 댓글을 삭제할 수 있다")
	void deleteComment() {
		// given
		ExtractableResponse<Response> createResponse =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "삭제할 댓글", "NON_ANONYMOUS");
		Long commentId = createResponse.jsonPath().getLong("data.commentId");

		// when
		ExtractableResponse<Response> response = 댓글_삭제_요청(userToken, commentId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("작성자가 아니면 댓글을 삭제할 수 없다")
	void deleteComment_denied_for_non_writer() {
		// given
		ExtractableResponse<Response> createResponse =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "삭제 불가 댓글", "NON_ANONYMOUS");
		Long commentId = createResponse.jsonPath().getLong("data.commentId");

		// when
		ExtractableResponse<Response> response = 댓글_삭제_요청(anotherUserToken, commentId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("대댓글을 생성할 수 있다")
	void createReplyComment() {
		// given
		ExtractableResponse<Response> parentResponse =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, SUPER_COMMENT_ID, "부모 댓글", "NON_ANONYMOUS");
		Long parentCommentId = parentResponse.jsonPath().getLong("data.commentId");

		// when
		ExtractableResponse<Response> response =
				댓글_생성_요청(userToken, PROGRAM_ID, TEAM_ID, parentCommentId, "대댓글입니다", "NON_ANONYMOUS");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
