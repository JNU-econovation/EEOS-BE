package com.blackcompany.eeos.comment.presentation.docs;

import com.blackcompany.eeos.comment.application.dto.CommandCommentResponse;
import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.QueryCommentsResponse;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "질문 및 댓글", description = "댓글 및 질문 관련 API")
public interface CommentApi {

	@Operation(summary = "질문 및 코멘트 작성", description = "코멘트 및 질문을 생성합니다.")
	ApiResponse<SuccessBody<CommandCommentResponse>> create(
			@Parameter(hidden = true) Long memberId, @RequestBody CreateCommentRequest request);

	@Operation(summary = "질문 및 코멘트 수정", description = "코멘트 및 질문을 수정합니다.")
	ApiResponse<SuccessBody<CommandCommentResponse>> update(
			@Parameter(hidden = true) Long memberId,
			@PathVariable("commentId") Long commentId,
			@RequestBody UpdateCommentRequest request);

	@Operation(summary = "질문 및 코멘트 삭제", description = "코멘트 및 질문을 삭제합니다.")
	ApiResponse<SuccessBody<Void>> delete(
			@Parameter(hidden = true) Long memberId, @PathVariable("commentId") Long commentId);

	@Operation(summary = "질문 및 코멘트 조회", description = "코멘트 및 질문을 조회합니다.")
	ApiResponse<SuccessBody<QueryCommentsResponse>> getComments(
			@Parameter(hidden = true) Long memberId,
			@RequestParam("programId") Long programId,
			@RequestParam("teamId") Long teamId);
}
