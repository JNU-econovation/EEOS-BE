package com.blackcompany.eeos.comment.application.service;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.converter.CommentResponseConverter;
import com.blackcompany.eeos.comment.application.exception.CommentUpdateFailedException;
import com.blackcompany.eeos.comment.application.exception.NotFoundCommentException;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.model.converter.CommentModelConverter;
import com.blackcompany.eeos.comment.application.usecase.CreateCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.DeleteCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.GetCommentUsecase;
import com.blackcompany.eeos.comment.application.usecase.UpdateCommentUsecase;
import com.blackcompany.eeos.comment.persistence.CommentEntity;
import com.blackcompany.eeos.comment.persistence.CommentRepository;
import com.blackcompany.eeos.comment.persistence.converter.CommentEntityConverter;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamException;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService
		implements CreateCommentUsecase, UpdateCommentUsecase, DeleteCommentUsecase, GetCommentUsecase {

	private final CommentRepository commentRepository;
	private final CommentModelConverter commentModelConverter;
	private final CommentEntityConverter commentEntityConverter;
	private final CommentResponseConverter commentResponseConverter;
	private final ProgramRepository programRepository;
	private final TeamRepository teamRepository;

	@Override
	public CommentModel create(Long memberId, CreateCommentRequest request) {
		CommentModel model = commentModelConverter.from(memberId, request);
		CommentEntity entity = createComment(model);
		return commentEntityConverter.from(entity);
	}

	@Override
	public CommentModel update(Long memberId, Long commentId, UpdateCommentRequest request) {
		CommentEntity updated = updateComment(commentId, request.getContent());
		return commentEntityConverter.from(updated);
	}

	@Override
	public void delete(Long commentId) {
		deleteComment(commentId);
	}

	@Override
	public List<CommentModel> getComments(Long memberId, Long programId, Long teamId) {
		return findComment(programId, teamId).stream()
				.map(commentEntityConverter::from)
				.filter(CommentModel::isSuperComment)
				.collect(Collectors.toList());
	}

	@Override
	public List<CommentModel> getAnswerComments(Long commentId) {
		return findAnswerComments(commentId).stream()
				.map(commentEntityConverter::from)
				.collect(Collectors.toList());
	}

	private List<CommentEntity> findAnswerComments(Long commentId) {
		return commentRepository.findCommentBySuperCommentId(commentId);
	}

	private List<CommentEntity> findComment(Long programId, Long teamId) {
		return commentRepository.findCommentByProgramIdAndPresentingTeamId(programId, teamId);
	}

	private void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	private CommentEntity createComment(CommentModel model) {
		createValidate(model);
		CommentEntity entity = commentEntityConverter.toEntity(model);
		return commentRepository.save(entity);
	}

	private CommentEntity updateComment(Long commentId, String content) {
		CommentEntity updated =
				commentRepository
						.updateById(commentId, content)
						.orElseThrow(() -> new CommentUpdateFailedException(commentId));
		return updated;
	}

	private CommentEntity findById(Long commentId) {
		return commentRepository
				.findById(commentId)
				.orElseThrow(() -> new NotFoundCommentException(commentId));
	}

	/** 이 기능은 model에 있어야 할까? */
	private void createValidate(CommentModel model) {
		Long teamId = model.getPresentingTeam();
		Long programId = model.getProgramId();

		if (!teamRepository.existsById(teamId)) {
			throw new NotFoundTeamException(teamId);
		}

		if (!programRepository.existsById(programId)) {
			throw new NotFoundProgramException(programId);
		}
	}
}
