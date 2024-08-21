package com.blackcompany.eeos.comment.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.comment.application.dto.*;
import com.blackcompany.eeos.comment.application.dto.converter.CommentResponseConverter;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.usecase.CreateCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.DeleteCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.GetCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.UpdateCommentUsecase;
import com.blackcompany.eeos.comment.presentation.docs.CommentApi;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.*;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentController implements CommentApi {

	private final CreateCommentUsecase createCommentUsecase;
	private final UpdateCommentUsecase updateCommentUsecase;
	private final GetCommentUsecase getCommentsUsecase;
	private final DeleteCommentUsecase deleteCommentUsecase;
	private final CommentResponseConverter responseConverter;
	private final CommentResponseConverter commentResponseConverter;

	@Override
	@PostMapping
	public ApiResponse<SuccessBody<CommandCommentResponse>> create(
			@Member Long memberId, @RequestBody CreateCommentRequest request) {
		CommentModel model = createCommentUsecase.create(memberId, request);
		return ApiResponseGenerator.success(
				responseConverter.from(model), HttpStatus.OK, MessageCode.CREATE);
	}

	@Override
	@PutMapping("/{commentId}")
	public ApiResponse<SuccessBody<CommandCommentResponse>> update(
			@Member Long memberId,
			@PathVariable("commentId") Long commentId,
			@RequestBody UpdateCommentRequest request) {
		CommentModel model = updateCommentUsecase.update(memberId, commentId, request);
		return ApiResponseGenerator.success(
				responseConverter.from(model), HttpStatus.OK, MessageCode.UPDATE);
	}

	@Override
	@DeleteMapping("/{commentId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("commentId") Long commentId) {
		deleteCommentUsecase.delete(memberId, commentId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@GetMapping
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

		return ApiResponseGenerator.success(
				commentResponseConverter.from(responses), HttpStatus.OK, MessageCode.GET);
	}
}
