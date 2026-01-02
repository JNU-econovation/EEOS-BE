package com.blackcompany.eeos.comment.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.comment.application.dto.CommandCommentResponse;
import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.QueryCommentResponse;
import com.blackcompany.eeos.comment.application.dto.QueryCommentsResponse;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.converter.CommentResponseConverter;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.usecase.CreateCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.DeleteCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.GetCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.UpdateCommentUsecase;
import com.blackcompany.eeos.comment.presentation.docs.CommentApi;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
						.toList();

		return ApiResponseGenerator.success(
				commentResponseConverter.from(responses), HttpStatus.OK, MessageCode.GET);
	}
}
