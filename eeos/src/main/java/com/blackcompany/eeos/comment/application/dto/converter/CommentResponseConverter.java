package com.blackcompany.eeos.comment.application.dto.converter;

import com.blackcompany.eeos.comment.application.dto.CommandCommentResponse;
import com.blackcompany.eeos.comment.application.dto.QueryAnswerResponse;
import com.blackcompany.eeos.comment.application.dto.QueryCommentResponse;
import com.blackcompany.eeos.comment.application.dto.QueryCommentsResponse;
import com.blackcompany.eeos.comment.application.exception.NotConvertedCommentException;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentResponseConverter {

	private final MemberRepository memberRepository;

	public CommandCommentResponse from(CommentModel source) {
		return CommandCommentResponse.builder().commentId(source.getId()).build();
	}

	public QueryCommentsResponse from(List<QueryCommentResponse> responses) {
		responses.stream().forEach(r -> r.getAnswers().stream().forEach(a -> a.toString()));
		return QueryCommentsResponse.builder().comments(responses).build();
	}

	public QueryCommentResponse from(Long memberId, CommentModel source, List<CommentModel> answers) {

		List<QueryAnswerResponse> answersResponse =
				answers.stream().map(e -> from(e, memberId)).collect(Collectors.toList());

		return QueryCommentResponse.builder()
				.time(source.getCreatedDate().toString())
				.content(source.getContent())
				.teamId(source.getPresentingTeam())
				.writer(findMemberName(memberId))
				.commentId(source.getId())
				.accessRight(source.getAccessRight(memberId))
				.answers(answersResponse)
				.build();
	}

	private QueryAnswerResponse from(CommentModel source, Long memberId) {
		if (source.getSuperCommentId() == -1) throw new NotConvertedCommentException();
		QueryAnswerResponse response =
				QueryAnswerResponse.builder()
						.commentId(source.getId())
						.content(source.getContent())
						.writer(findMemberName(source.getWriter()))
						.time(source.getCreatedDate().toString())
						.accessRight(source.getAccessRight(memberId))
						.build();
		return response;
	}

	private String findMemberName(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new).getName();
	}
}
