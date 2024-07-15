package com.blackcompany.eeos.comment.application.service;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.converter.CommentResponseConverter;
import com.blackcompany.eeos.comment.application.exception.DeniedCommentEditException;
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
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Override
	public CommentModel create(Long memberId, CreateCommentRequest request) {
		CommentModel model = commentModelConverter.from(memberId, request);
		createValidate(model);
		CommentModel saved = createComment(model);
		return saved;
	}

	@Transactional
	@Override
	public CommentModel update(Long memberId, Long commentId, UpdateCommentRequest request) {
		CommentModel model = findCommentById(commentId);
		model.validateUpdate(memberId);
		CommentModel updated = updateComment(commentId, request.getContent());
		return updated;
	}

	@Transactional
	@Override
	public void delete(Long memberId, Long commentId) {
		CommentModel model = findCommentById(commentId);
		model.validateDelete(memberId);
		deleteComment(commentId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CommentModel> getComments(Long memberId, Long programId, Long teamId) {
		if (teamId == null) {
			return findCommentsByProgramId(programId);
		}

		return findCommentsByProgramIdAndTeam(programId, teamId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CommentModel> getAnswerComments(Long commentId) {
		return findAnswerCommentsBySuperId(commentId).stream()
				.map(commentEntityConverter::from)
				.collect(Collectors.toList());
	}

	private void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	private CommentModel createComment(CommentModel model) {
		CommentEntity entity = commentEntityConverter.toEntity(model);
		CommentEntity saved = commentRepository.save(entity);
		return commentEntityConverter.from(saved);
	}

	private CommentModel updateComment(Long commentId, String content) {
		CommentModel updated =
				commentRepository
						.updateById(commentId, content)
						.map(commentEntityConverter::from)
						.orElseThrow(() -> new DeniedCommentEditException(commentId));
		return updated;
	}

	private CommentModel findCommentById(Long commentId) {

		return commentRepository
				.findById(commentId)
				.map(commentEntityConverter::from)
				.orElseThrow(() -> new NotFoundCommentException(commentId));
	}

	private List<CommentEntity> findAnswerCommentsBySuperId(Long commentId) {
		return commentRepository.findCommentBySuperCommentId(commentId);
	}

	private List<CommentModel> findCommentsByProgramIdAndTeam(Long programId, Long teamId) {
		return commentRepository.findCommentByProgramIdAndPresentingTeamId(programId, teamId).stream()
				.map(commentEntityConverter::from)
				.collect(Collectors.toList());
	}

	private List<CommentModel> findCommentsByProgramId(Long programId) {
		return commentRepository.findCommentByProgramId(programId).stream()
				.map(commentEntityConverter::from)
				.collect(Collectors.toList());
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

		model.validateCreate();
	}
}
