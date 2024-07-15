package com.blackcompany.eeos.comment.presentation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.comment.application.dto.*;
import com.blackcompany.eeos.comment.application.dto.converter.CommentResponseConverter;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.usecase.CreateCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.DeleteCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.GetCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.UpdateCommentUsecase;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.*;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CreateCommentUsecase createCommentUsecase;
	private final UpdateCommentUsecase updateCommentUsecase;
	private final GetCommentUsecase getCommentsUsecase;
	private final DeleteCommentUsecase deleteCommentUsecase;
	private final CommentResponseConverter responseConverter;
	private final CommentResponseConverter commentResponseConverter;

	@PostMapping
	@Operation(summary = "질문 및 코멘트 작성", description = "코멘트 및 질문을 생성합니다.")
	public ApiResponse<SuccessBody<CommandCommentResponse>> create(
			@Member Long memberId, @RequestBody CreateCommentRequest request) {
		CommentModel model = createCommentUsecase.create(memberId, request);
		return ApiResponseGenerator.success(
				responseConverter.from(model), HttpStatus.OK, MessageCode.CREATE);
	}

	@PutMapping("/{commentId}")
	@Operation(summary = "질문 및 코멘트 수정", description = "코멘트 및 질문을 수정합니다.")
	public ApiResponse<SuccessBody<CommandCommentResponse>> update(
			@Member Long memberId,
			@PathVariable("commentId") Long commentId,
			@RequestBody UpdateCommentRequest request) {
		CommentModel model = updateCommentUsecase.update(memberId, commentId, request);
		return ApiResponseGenerator.success(
				responseConverter.from(model), HttpStatus.OK, MessageCode.UPDATE);
	}

	@DeleteMapping("/{commentId}")
	@Operation(summary = "질문 및 코멘트 삭제", description = "코멘트 및 질문을 삭제합니다.")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("commentId") Long commentId) {
		deleteCommentUsecase.delete(memberId, commentId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@GetMapping
	@Operation(summary = "질문 및 코멘트 조회", description = "코멘트 및 질문을 조회합니다.")
	public ApiResponse<SuccessBody<QueryCommentsResponse>> getComments(
			@Member Long memberId,
			@RequestParam("programId") Long programId,
			@RequestParam("teamId") Long teamId) {
		List<CommentModel> comments = getCommentsUsecase.getComments(memberId, programId, teamId);
		List<QueryCommentResponse> responses =
				comments.stream()
						.map(
								e ->
										commentResponseConverter.from(
												memberId, e, getCommentsUsecase.getAnswerComments(e.getId())))
						.collect(Collectors.toList());

		return ApiResponseGenerator.success(commentResponseConverter.from(responses), HttpStatus.OK, MessageCode.GET);
	}
}
