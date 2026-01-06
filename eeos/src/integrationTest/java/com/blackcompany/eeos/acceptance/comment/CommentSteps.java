package com.blackcompany.eeos.acceptance.comment;

import static com.blackcompany.eeos.acceptance.common.AcceptanceTestSteps.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class CommentSteps {

	private static final String BASE_PATH = "/api/comments";

	public static ExtractableResponse<Response> 댓글_생성_요청(
			String token,
			Long programId,
			Long teamId,
			Long parentsCommentId,
			String content,
			String commentType) {
		Map<String, Object> body = new HashMap<>();
		body.put("programId", programId);
		body.put("teamId", teamId);
		body.put("parentsCommentId", parentsCommentId);
		body.put("content", content);
		body.put("commentType", commentType);

		return post(BASE_PATH, body, token);
	}

	public static ExtractableResponse<Response> 댓글_목록_조회_요청(
			String token, Long programId, Long teamId) {
		String path = String.format("%s?programId=%d&teamId=%d", BASE_PATH, programId, teamId);
		return get(path, token);
	}

	public static ExtractableResponse<Response> 댓글_수정_요청(
			String token, Long commentId, String contents) {
		Map<String, Object> body = new HashMap<>();
		body.put("contents", contents);

		return put(BASE_PATH + "/" + commentId, body, token);
	}

	public static ExtractableResponse<Response> 댓글_삭제_요청(String token, Long commentId) {
		return delete(BASE_PATH + "/" + commentId, token);
	}
}
