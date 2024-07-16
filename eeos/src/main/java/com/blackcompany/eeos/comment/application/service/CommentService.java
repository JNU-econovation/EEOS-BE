package com.blackcompany.eeos.comment.application.service;

import com.blackcompany.eeos.comment.application.dto.CreateCommentRequest;
import com.blackcompany.eeos.comment.application.dto.UpdateCommentRequest;
import com.blackcompany.eeos.comment.application.exception.NotCreateAdminCommentException;
import com.blackcompany.eeos.comment.application.exception.NotExpectedCommentEditException;
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
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
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
	private final ProgramRepository programRepository;
	private final TeamRepository teamRepository;
	private final QueryMemberService memberService;

	@Transactional
	@Override
	public CommentModel create(Long memberId, CreateCommentRequest request) {
		validateUser(memberId);
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

		return findCommentById(updateComment(commentId, request.getContents()));
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
			throw new NullPointerException("teamId 값이 null 입니다.");
		}

		if (programId == null) {
			throw new NullPointerException("programId 값이 null 입니다.");
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
		if (!model.isSuperComment()) changeSuperComment(model);
		CommentEntity entity = commentEntityConverter.toEntity(model);
		CommentEntity saved = commentRepository.save(entity);
		return commentEntityConverter.from(saved);
	}

	private void changeSuperComment(CommentModel model) {
		Long superCommentId = findCommentById(model.getSuperCommentId()).getSuperCommentId();
		model.changeSuperComment(superCommentId);
	}

	private Long updateComment(Long commentId, String content) {
		int result = commentRepository.updateById(commentId, content);
		if (result == 0) throw new NotExpectedCommentEditException();

		return commentId;
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
				.filter(CommentModel::isSuperComment)
				.collect(Collectors.toList());
	}

	private void validateUser(Long memberId) {
		MemberModel member = memberService.findMember(memberId);
		if (member.isAdmin()) throw new NotCreateAdminCommentException();
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
